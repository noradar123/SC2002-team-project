package data;

import entity.Internship;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for managing the storage and retrieval of Internship entities.
 * This class acts as an in-memory database for internship listings, supporting CRUD operations.
 */
public class InternshipRepository {
    
    private final List<Internship> internships = new ArrayList<>();

    /**
     * Constructs a new InternshipRepository.
     * Initializes an empty list for storage.
     */
    public InternshipRepository() {
        // Default constructor
    }

    /**
     * Adds a new internship listing to the repository.
     *
     * @param internship The internship object to add.
     * @throws IllegalArgumentException If the provided internship is null.
     */
    public void add(Internship internship) throws IllegalArgumentException {
        if (internship == null) throw new IllegalArgumentException("Internship cannot be null");
        internships.add(internship);
    }

    /**
     * Removes an internship from the repository based on its unique ID.
     *
     * @param id The unique ID of the internship to remove.
     * @return {@code true} if an internship was found and removed; {@code false} otherwise.
     */
    public boolean remove(String id) {
        return internships.removeIf(i -> i.getId().equals(id)); 
    }

    /**
     * Retrieves all internships currently stored in the repository.
     *
     * @return A new list containing all internships (read-only copy).
     */
    public List<Internship> all() {
        return new ArrayList<>(internships);
    }

    /**
     * Finds a specific internship by its unique ID.
     *
     * @param id The unique ID string.
     * @return The {@link Internship} object if found; {@code null} otherwise.
     */
    public Internship findById(String id) {
        return internships.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates an existing internship in the repository by replacing its fields.
     * Finds the existing record by ID and applies changes from the provided object.
     *
     * @param updated The internship object containing updated data (must have a valid ID).
     * @return The updated {@link Internship} object (useful for UI updates).
     * @throws IllegalArgumentException If the internship does not exist in the repository.
     */
    public Internship update(Internship updated) throws IllegalArgumentException {
        Internship existing = findById(updated.getId());
        if (existing == null) {
            throw new IllegalArgumentException("Internship not found: " + updated.getId());
        }
        existing.updateFrom(updated);
        return existing; 
    }
}