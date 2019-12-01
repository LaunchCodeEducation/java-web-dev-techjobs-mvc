package org.launchcode.javawebdevtechjobsmvc.controllers;

import org.launchcode.javawebdevtechjobsmvc.models.Job;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.launchcode.javawebdevtechjobsmvc.models.JobData;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "list")
public class ListController {

    static HashMap<String, String> columnChoices = new HashMap<>();

    public ListController () {
        columnChoices.put("coreCompetency", "Skill");
        columnChoices.put("employer", "Employer");
        columnChoices.put("location", "Location");
        columnChoices.put("positionType", "Position Type");
        columnChoices.put("all", "All");
    }

    @RequestMapping(value = "")
    public String list(Model model) {

        model.addAttribute("columns", columnChoices);

        return "list";
    }

    @RequestMapping(value = "values")
    public String listColumnValues(Model model, @RequestParam String column) {

        if (column.equals("all")) {
            ArrayList<Job> jobs = JobData.findAll();
            model.addAttribute("title", "All Jobs");
            model.addAttribute("jobs", jobs);
            return "list-jobs";
        } else {
            ArrayList<String> items = JobData.findAll(column);
            model.addAttribute("title", "All " + columnChoices.get(column) + " Values");
            model.addAttribute("column", column);
            model.addAttribute("items", items);
            return "list-column";
        }

    }

    @RequestMapping(value = "jobs")
    public String listJobsByColumnAndValue(Model model, @RequestParam String column, @RequestParam String value) {

        ArrayList<Job> jobs = JobData.findByColumnAndValue(column, value);
        model.addAttribute("title", "Jobs with " + columnChoices.get(column) + ": " + value);
        model.addAttribute("jobs", jobs);

        return "list-jobs";
    }
}
