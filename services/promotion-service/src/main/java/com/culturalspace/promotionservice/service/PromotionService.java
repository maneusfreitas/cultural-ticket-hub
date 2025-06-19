package com.culturalspace.promotionservice.service;

import com.culturalspace.promotionservice.model.Promotion;
import com.culturalspace.promotionservice.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Transactional
    public Promotion createPromotion(Promotion promotion) {
        if (promotion.getTimesUsed() == null) {
            promotion.setTimesUsed(0);
        }
        System.out.println("PromotionService: Creating new promotion with code: " + promotion.getCode() + (promotion.getEventId() != null ? " for event ID: " + promotion.getEventId() : " (universal)"));
        return promotionRepository.save(promotion);
    }

    @Transactional(readOnly = true)
    public Optional<Promotion> getPromotionByCode(String code) {
        System.out.println("PromotionService: Fetching promotion with code: " + code);
        return promotionRepository.findByCode(code);
    }

    @Transactional(readOnly = true)
    public List<Promotion> getAllPromotions() {
        System.out.println("PromotionService: Fetching all promotions.");
        return promotionRepository.findAll();
    }

    @Transactional
    public Promotion updatePromotion(Long id, Promotion updatedPromotion) {
        System.out.println("PromotionService: Updating promotion with ID: " + id);
        return promotionRepository.findById(id)
                .map(existingPromotion -> {
                    existingPromotion.setCode(updatedPromotion.getCode());
                    existingPromotion.setEventId(updatedPromotion.getEventId());
                    existingPromotion.setDiscountValue(updatedPromotion.getDiscountValue());
                    existingPromotion.setStartDate(updatedPromotion.getStartDate());
                    existingPromotion.setEndDate(updatedPromotion.getEndDate());
                    existingPromotion.setUsageLimit(updatedPromotion.getUsageLimit());
                    existingPromotion.setActive(updatedPromotion.isActive());
                    return promotionRepository.save(existingPromotion);
                })
                .orElseThrow(() -> new RuntimeException("Promotion not found with ID: " + id));
    }

    @Transactional
    public void deletePromotion(Long id) {
        System.out.println("PromotionService: Deleting promotion with ID: " + id);
        promotionRepository.deleteById(id);
    }

    @Transactional
    public Double applyDiscount(Double originalAmount, String promoCode, Long eventId) { // Added eventId parameter
        if (promoCode == null || promoCode.trim().isEmpty()) {
            System.out.println("PromotionService: No promo code provided. Returning original amount.");
            return originalAmount;
        }

        Optional<Promotion> optionalPromotion = getPromotionByCode(promoCode);
        if (optionalPromotion.isEmpty()) {
            System.out.println("PromotionService: Promo code '" + promoCode + "' not found.");
            return originalAmount;
        }

        Promotion promotion = optionalPromotion.get();

        // New validation: Check if promo is for a specific event and matches the given eventId
        if (promotion.getEventId() != null) { // If promotion is event-specific
            if (eventId == null || !promotion.getEventId().equals(eventId)) {
                System.out.println("PromotionService: Promo code '" + promoCode + "' is specific to event ID " + promotion.getEventId() + " but applied for event ID " + eventId + ". Not applicable.");
                return originalAmount;
            }
        }

        // Existing validations
        if (!promotion.isActive() || LocalDate.now().isBefore(promotion.getStartDate()) || LocalDate.now().isAfter(promotion.getEndDate())) {
            System.out.println("PromotionService: Promo code '" + promoCode + "' is not active or expired. Current date: " + LocalDate.now());
            return originalAmount;
        }
        if (promotion.getUsageLimit() != null && promotion.getTimesUsed() >= promotion.getUsageLimit()) {
            System.out.println("PromotionService: Promo code '" + promoCode + "' has reached its usage limit.");
            return originalAmount;
        }

        // Apply discount
        Double discountedAmount;
        if ("PERCENTAGE".equalsIgnoreCase(promotion.getType())) {
            discountedAmount = originalAmount * (1 - promotion.getDiscountValue());
            System.out.println("PromotionService: Applied " + (promotion.getDiscountValue() * 100) + "% discount for code '" + promoCode + "'. Original: " + originalAmount + ", Final: " + discountedAmount);
        } else if ("FIXED_AMOUNT".equalsIgnoreCase(promotion.getType())) {
            discountedAmount = Math.max(0.0, originalAmount - promotion.getDiscountValue());
            System.out.println("PromotionService: Applied fixed discount of " + promotion.getDiscountValue() + " for code '" + promoCode + "'. Original: " + originalAmount + ", Discounted: " + discountedAmount);
        } else {
            System.err.println("PromotionService: Unknown promotion type: " + promotion.getType() + ". Returning original amount.");
            return originalAmount;
        }

        incrementUsage(promoCode);

        return discountedAmount;
    }

    @Transactional
    public void incrementUsage(String code) {
        System.out.println("PromotionService: Attempting to increment usage for promo code: " + code);
        getPromotionByCode(code).ifPresent(promotion -> {
            promotion.setTimesUsed(promotion.getTimesUsed() + 1);
            promotionRepository.save(promotion);
            System.out.println("PromotionService: Usage for promo code '" + code + "' incremented to " + promotion.getTimesUsed());
        });
    }
}