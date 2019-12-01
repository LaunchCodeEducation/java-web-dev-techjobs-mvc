package org.launchcode.javawebdevtechjobsmvc.models;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "job_data.csv";
    private static boolean isDataLoaded = false;

    //private static ArrayList<HashMap<String, String>> allJobs;
    private static ArrayList<Job> allJobs;
    private static ArrayList<Employer> allEmployers;
    private static ArrayList<Location> allLocations;
    private static ArrayList<PositionType> allPositionTypes;
    private static ArrayList<CoreCompetency> allCoreCompetency;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();
        String aValue;

        for (Job job : allJobs) {
            if (field.equals("name")){
                aValue = job.getName();
            } else if (field.equals("employer")){
                aValue = job.getEmployer().toString();
            } else if (field.equals("location")){
                aValue = job.getLocation().toString();
            } else if (field.equals("positionType")){
                aValue = job.getPositionType().toString();
            } else {
                aValue = job.getCoreCompetency().toString();
            }

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        // Bonus mission: sort the results
        Collections.sort(values);

        return values;
    }

    public static ArrayList<Job> findAll() {

        // load data, if not already loaded
        loadData();

        // Bonus mission; normal version returns allJobs
        return new ArrayList<Job>(allJobs);
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);

            if (aValue != null && aValue.toLowerCase().contains(value.toLowerCase())) {
                jobs.add(row);
            }
        }

        return jobs;
    }

    /**
     * Search all columns for the given term
     *
     * @param value The search term to look for
     * @return      List of all jobs with at least one field containing the value
     */
    public static ArrayList<HashMap<String, String>> findByValue(String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {

            for (String key : row.keySet()) {
                String aValue = row.get(key);

                if (aValue.toLowerCase().contains(value.toLowerCase())) {
                    jobs.add(row);

                    // Finding one field in a job that matches is sufficient
                    break;
                }
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
//                HashMap<String, String> newJob = new HashMap<>();
//
//                for (String headerLabel : headers) {
//                    newJob.put(headerLabel, record.get(headerLabel));
//                }

                Employer anEmployer = new Employer(headers[1]);
                Location aLocation = new Location(headers[2]);
                PositionType aPosition = new PositionType(headers[3]);
                CoreCompetency aCoreCopetency = new CoreCompetency(headers[4]);

                Job tempJob = new Job(headers[0], anEmployer, aLocation, aPosition, aCoreCopetency);

                allJobs.add(tempJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}

