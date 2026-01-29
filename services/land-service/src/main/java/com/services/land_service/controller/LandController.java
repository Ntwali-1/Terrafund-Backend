package com.services.land_service.controller;

import com.services.land_service.dto.*;
import com.services.land_service.entity.Land.LandStatus;
import com.services.land_service.service.LandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lands")
@RequiredArgsConstructor
public class LandController {

    private final LandService landService;

    /**
     * Create a new land
     * POST /api/lands
     */
    @PostMapping
    public ResponseEntity<LandResponse> createLand(@Valid @RequestBody CreateLandRequest request) {
        LandResponse response = landService.createLand(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Get land by ID
     * GET /api/lands/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<LandResponse> getLandById(@PathVariable Long id) {
        LandResponse response = landService.getLandById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all lands with pagination and sorting
     * GET /api/lands?page=0&size=10&sortBy=createdAt&sortDir=DESC
     */
    @GetMapping
    public ResponseEntity<Page<LandSummaryResponse>> getAllLands(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<LandSummaryResponse> response = landService.getAllLands(pageable);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LandResponse> updateLand(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLandRequest request) {

        LandResponse response = landService.updateLand(id, request);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<LandResponse> updateLandStatus(
            @PathVariable Long id,
            @RequestParam LandStatus status) {

        LandResponse response = landService.updateLandStatus(id, status);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete land
     * DELETE /api/lands/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLand(@PathVariable Long id) {
        landService.deleteLand(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Search lands by location
     * GET /api/lands/search/location?province=Kigali&district=Gasabo&sector=Remera
     */
    @GetMapping("/search/location")
    public ResponseEntity<Page<LandSummaryResponse>> searchLandsByLocation(
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String district,
            @RequestParam(required = false) String sector,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<LandSummaryResponse> response = landService.searchLandsByLocation(
                province, district, sector, pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Get lands by status
     * GET /api/lands/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<LandSummaryResponse>> getLandsByStatus(
            @PathVariable LandStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<LandSummaryResponse> response = landService.getLandsByStatus(status, pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Get lands by area range
     * GET /api/lands/search/area?minArea=1000&maxArea=5000
     */
    @GetMapping("/search/area")
    public ResponseEntity<Page<LandSummaryResponse>> getLandsByAreaRange(
            @RequestParam Double minArea,
            @RequestParam Double maxArea,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<LandSummaryResponse> response = landService.getLandsByAreaRange(
                minArea, maxArea, pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * Add images to land
     * POST /api/lands/{id}/images
     */
    @PostMapping("/{id}/images")
    public ResponseEntity<LandResponse> addImages(
            @PathVariable Long id,
            @Valid @RequestBody UploadImagesRequest request) {

        LandResponse response = landService.addImages(id, request.getImageUrls());
        return ResponseEntity.ok(response);
    }

    /**
     * Add documents to land
     * POST /api/lands/{id}/documents
     */
    @PostMapping("/{id}/documents")
    public ResponseEntity<LandResponse> addDocuments(
            @PathVariable Long id,
            @Valid @RequestBody UploadDocumentsRequest request) {

        LandResponse response = landService.addDocuments(id, request.getDocumentUrls());
        return ResponseEntity.ok(response);
    }

    /**
     * Remove image from land
     * DELETE /api/lands/{id}/images
     */
    @DeleteMapping("/{id}/images")
    public ResponseEntity<LandResponse> removeImage(
            @PathVariable Long id,
            @RequestParam String imageUrl) {

        LandResponse response = landService.removeImage(id, imageUrl);
        return ResponseEntity.ok(response);
    }

    /**
     * Remove document from land
     * DELETE /api/lands/{id}/documents
     */
    @DeleteMapping("/{id}/documents")
    public ResponseEntity<LandResponse> removeDocument(
            @PathVariable Long id,
            @RequestParam String documentUrl) {

        LandResponse response = landService.removeDocument(id, documentUrl);
        return ResponseEntity.ok(response);
    }
}