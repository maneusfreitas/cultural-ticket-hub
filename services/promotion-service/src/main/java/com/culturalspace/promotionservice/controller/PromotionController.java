package com.culturalspace.promotionservice.controller;

import com.culturalspace.promotionservice.model.Promotion;
import com.culturalspace.promotionservice.service.PromotionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping
    public ResponseEntity<Promotion> createPromotion(@RequestBody Promotion promotion) {
        try {
            Promotion newPromotion = promotionService.createPromotion(promotion);
            return new ResponseEntity<>(newPromotion, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.err.println("Controller error during promotion creation: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<Promotion> getPromotionByCode(@PathVariable String code) {
        Optional<Promotion> promotion = promotionService.getPromotionByCode(code);
        return promotion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Promotion>> getAllPromotions() {
        List<Promotion> promotions = promotionService.getAllPromotions();
        return ResponseEntity.ok(promotions);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable Long id, @RequestBody Promotion promotion) {
        try {
            Promotion updatedPromotion = promotionService.updatePromotion(id, promotion);
            return ResponseEntity.ok(updatedPromotion);
        } catch (RuntimeException e) {
            System.err.println("Controller error during promotion update: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Long id) {
        try {
            promotionService.deletePromotion(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            System.err.println("Controller error during promotion deletion: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/apply")
    public ResponseEntity<Double> applyDiscount(@RequestParam Double originalAmount,
                                                @RequestParam(required = false) String promoCode,
                                                @RequestParam(required = false) Long eventId) { // Added optional eventId
        try {
            if (originalAmount == null || originalAmount < 0) {
                System.err.println("Controller error during discount application: Original amount is invalid.");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            Double discountedAmount = promotionService.applyDiscount(originalAmount, promoCode, eventId); // Pass eventId
            return ResponseEntity.ok(discountedAmount);
        } catch (RuntimeException e) {
            System.err.println("Controller error during discount application: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}