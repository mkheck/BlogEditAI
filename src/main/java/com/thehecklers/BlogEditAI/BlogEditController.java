package com.thehecklers.BlogEditAI;

import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/api/edit")
    public DraftCritique editDraft(@RequestBody DraftRequestSpec draftRequestSpec) {
        return editorService.critiqueDraft(draftRequestSpec);
    }
}
