package com.thehecklers.BlogEditAI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class BlogEditorService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ChatClient client;

    public BlogEditorService(ChatClient.Builder builder) {
        this.client = builder.build();
    }

    public DraftCritique critiqueDraft(DraftRequestSpec draftspec) {
        logger.info("Beginning critique...");

        var approved = false;
        var prompt = String.format("""
                You are a critical blog editor with extremely high standards. Evaluate the following blog draft and respond with either:
                BLOG_PASS - if the draft is exceptional, well-written, engaging, and complete
                BLOG_FAIL - followed by specific, actionable feedback on what to improve
                
                Focus on:
                - Clarity and flow of ideas
                - Engagement and reader interest
                - Professional yet conversational tone
                - Structure and organization
                - Strict adherence to the specified maximum length requirement
                
                IMPORTANT EVALUATION RULES:
                1. The blog MUST have no more than %d sentences total. Count the sentences carefully.
                2. If the draft exceeds the specified maximum sentence count, it must receive a BLOG_FAIL rating.
                3. Be extremely thorough in your evaluation and provide detailed feedback.
                4. Even well-written drafts should receive suggestions for improvement in early iterations.
                
                Draft:
                %s
                """, draftspec.maxSentences(), draftspec.draft());

        logger.info("Sending draft for editorial evaluation");
        var feedback = client.prompt()
                .user(prompt)
                .call()
                .content();

        var draftCritique = extractFeedback(feedback);
        logger.info("Critique received : {}", draftCritique.critique());

        if (draftCritique.approval()) {
            logger.info("Draft approved by editor");
        } else {
            logger.warn("Draft rejected by editor");
        }

        return draftCritique;
    }

    private DraftCritique extractFeedback(@NotNull String evaluation) {
        var pass = evaluation.toUpperCase().contains("BLOG_PASS");
        var grade = pass ? "BLOG_PASS" : "BLOG_FAIL";

        // Get the index of the actual evaluation status (after BLOG_PASS or BLOG_FAIL)
        int idx = evaluation.toUpperCase().indexOf(grade) + grade.length();

        return new DraftCritique(pass,
                evaluation.substring(idx).trim());
    }
}