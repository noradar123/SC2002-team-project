package control;

import boundary.FilterView;
import entity.User;
import entity.Internship;
import enums.InternshipLevel;
import enums.InternshipStatus;
import filter.StatusFilter;
import filter.LevelFilter;
import filter.MajorFilter;
import filter.VisibleFilter;
import filter.CurrentlyOpenFilter;
import filter.YearFilter;
import service.InternshipService;

import java.util.List;

public class FilterController {
    private final FilterView view;
    private final InternshipService internshipService;

    public FilterController(FilterView view, InternshipService internshipService) {
        this.view = view;
        this.internshipService = internshipService;
    }

    public void manageFiltersFor(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        while (true) {
            int opt = view.promptFilterMenu();
            if (opt == 0) return;
            switch (opt) {
                case 1: {
                    String s = view.promptStatus();
                    try {
                        InternshipStatus status = InternshipStatus.valueOf(s.toUpperCase());
                        user.getFilter().addFilter(new StatusFilter(status));
                        view.show("Added status filter: " + status);
                    } catch (IllegalArgumentException e) {
                        view.show("Invalid status: " + s);
                    }
                    break;
                }
                case 2: {
                    String s = view.promptLevel();
                    try {
                        InternshipLevel lvl = InternshipLevel.valueOf(s.toUpperCase());
                        user.getFilter().addFilter(new LevelFilter(lvl));
                        view.show("Added level filter: " + lvl);
                    } catch (IllegalArgumentException e) {
                        view.show("Invalid level: " + s);
                    }
                    break;
                }
                case 3: {
                    String maj = view.promptMajor();
                    if (maj == null || maj.trim().isEmpty()) { view.show("Major cannot be empty"); break; }
                    user.getFilter().addFilter(new MajorFilter(maj));
                    view.show("Added major filter: " + maj);
                    break;
                }
                case 4: {
                    user.getFilter().addFilter(new VisibleFilter());
                    view.show("Added visible-only filter.");
                    break;
                }
                case 5: {
                    user.getFilter().addFilter(new CurrentlyOpenFilter());
                    view.show("Added currently-open-only filter.");
                    break;
                }
                case 6: {
                    int y = view.promptMinYear();
                    user.getFilter().addFilter(new YearFilter(y));
                    view.show("Added year filter: min year " + y);
                    break;
                }
                case 7: {
                    user.getFilter().clear();
                    view.show("Cleared filters to defaults.");
                    break;
                }
                default:
                    view.show("Unknown option.");
            }

            // after each change show internships matching new filters
            List<Internship> filtered = internshipService.getInternshipsFor(user);
            view.showInternships(filtered);
        }
    }
}
