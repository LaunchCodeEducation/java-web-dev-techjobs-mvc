package org.launchcode.javawebdevtechjobsmvc.models;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.launchcode.javawebdevtechjobsmvc.NameSorter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

// This is a change made in sandbox.

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "job_data.csv";
    private static boolean isDataLoaded = false;

    private static ArrayList<Job> allJobs;
    private static ArrayList<Employer> allEmployers = new ArrayList<>();
    private static ArrayList<Location> allLocations = new ArrayList<>();
    private static ArrayList<PositionType> allPositionTypes = new ArrayList<>();
    private static ArrayList<CoreCompetency> allCoreCompetency = new ArrayList<>();
    private static ArrayList<String> employerNames = new ArrayList<>();
    private static ArrayList<String> locations = new ArrayList<>();
    private static ArrayList<String> skills = new ArrayList<>();
    private static ArrayList<String> positions = new ArrayList<>();

    /**
     * Fetch list of all job objects from loaded data,
     * without duplicates, then return a copy.
     */

    public static ArrayList<Job> findAll() {

        // load data, if not already loaded
        loadData();

        // Bonus mission; normal version returns allJobs
        return new ArrayList<>(allJobs);
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of the field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<Job> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<Job> jobs = new ArrayList<>();

        if (column.equals("all")){
            jobs = findByValue(value);
            return jobs;
        }
        for (Job job : allJobs) {

            String aValue = getFieldValue(job, column);

            if (aValue != null && aValue.toLowerCase().contains(value.toLowerCase())) {
                jobs.add(job);
            }
        }

        return jobs;
    }

    public static String getFieldValue(Job job, String fieldName){
        String theValue;
        if (fieldName.equals("name")){
            theValue = job.getName();
        } else if (fieldName.equals("employer")){
            theValue = job.getEmployer().toString();
        } else if (fieldName.equals("location")){
            theValue = job.getLocation().toString();
        } else if (fieldName.equals("positionType")){
            theValue = job.getPositionType().toString();
        } else {
            theValue = job.getCoreCompetency().toString();
        }

        return theValue;
    }
    /**
     * Search all columns for the given term
     *
     * @param value The search term to look for
     * @return      List of all jobs with at least one field containing the value
     */
    public static ArrayList<Job> findByValue(String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<Job> jobs = new ArrayList<>();

        for (Job job : allJobs) {

            if (job.getName().toLowerCase().contains(value.toLowerCase())) {
                jobs.add(job);
            } else if (job.getEmployer().toString().toLowerCase().contains(value.toLowerCase())) {
                jobs.add(job);
            } else if (job.getLocation().toString().toLowerCase().contains(value.toLowerCase())) {
                jobs.add(job);
            } else if (job.getPositionType().toString().toLowerCase().contains(value.toLowerCase())) {
                jobs.add(job);
            } else if (job.getCoreCompetency().toString().toLowerCase().contains(value.toLowerCase())) {
                jobs.add(job);
            }

        }

        return jobs;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Resource resource = new ClassPathResource(DATA_FILE);
            InputStream is = resource.getInputStream();
            Reader reader = new InputStreamReader(is);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(reader);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {

                String anEmployer = record.get(1);
                String aLocation = record.get(2);
                String aPosition = record.get(3);
                String aSkill = record.get(4);
                if (!employerNames.contains(anEmployer)){
                    employerNames.add(anEmployer);
                    Employer newEmployer = new Employer(anEmployer);
                    allEmployers.add(newEmployer);
                }
                if (!locations.contains(aLocation)){
                    locations.add(aLocation);
                    Location newLocation = new Location(aLocation);
                    allLocations.add(newLocation);
                }
                if (!skills.contains(aSkill)){
                    skills.add(aSkill);
                    CoreCompetency newSkill = new CoreCompetency(aSkill);
                    allCoreCompetency.add(newSkill);
                }
                if (!positions.contains(aPosition)){
                    positions.add(aPosition);
                    PositionType newPosition = new PositionType(aPosition);
                    allPositionTypes.add(newPosition);
                }

                Employer newEmployer = new Employer(record.get(1));
                Location newLocation = new Location(record.get(2));
                PositionType newPosition = new PositionType(record.get(3));
                CoreCompetency aCoreCompetency = new CoreCompetency(record.get(4));

                Job newJob = new Job(record.get(0), newEmployer, newLocation, newPosition, aCoreCompetency);

                allJobs.add(newJob);
            }
            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

    public static ArrayList<Employer> getAllEmployers() {
        loadData();
        allEmployers.sort(new NameSorter());
        return allEmployers;
    }

    public static ArrayList<Location> getAllLocations() {
        loadData();
        allLocations.sort(new NameSorter());
        return allLocations;
    }

    public static ArrayList<PositionType> getAllPositionTypes() {
        loadData();
        allPositionTypes.sort(new NameSorter());
        return allPositionTypes;
    }

    public static ArrayList<CoreCompetency> getAllCoreCompetency() {
        loadData();
        allCoreCompetency.sort(new NameSorter());
        return allCoreCompetency;
    }

}

