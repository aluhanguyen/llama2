package com.oby.ai.llama.controller;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Map;

/**
 * @author OBY.Mike
 * Created on 24/10/24.
 */
@RestController
public class ChatController {
    private final OllamaChatModel chatModel;

    @Autowired
    public ChatController(OllamaChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @GetMapping("/ai/generate")
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", chatModel.call(message));
    }

    @GetMapping(value = "/ai/generateStream")
    public Flux<String> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        // Trả về Flux, từng phần của câu sẽ được gửi liên tiếp tới frontend
        return chatModel.stream(prompt)
                .map(response -> response.getResult().getOutput().getContent())
                .concatWith(Flux.just("[DONE]")); // Trích xuất nội dung từng phần

    }

    @GetMapping("/ai/test")
    public Flux<String> generateStringStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Flux.fromIterable(Arrays.asList("Message 1", "Message 2", "Message 3"));
    }

}
