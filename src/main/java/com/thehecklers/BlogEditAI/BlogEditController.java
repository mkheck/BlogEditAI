package com.thehecklers.BlogEditAI;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogEditController {
    private final BlogEditorService editorService;

    public BlogEditController(BlogEditorService editorService) {
        this.editorService = editorService;
    }

    @GetMapping
    public String liveness() {
        return "Editor service is alive and well.";
    }

    // GET would be more appropriate since this is idempotent, but POST is used since GET has 2048 char limit
    @PostMapping("/api/edit")
    public DraftCritique editDraft(@RequestBody DraftRequestSpec draftRequestSpec) {
        return editorService.critiqueDraft(draftRequestSpec);
    }
}
