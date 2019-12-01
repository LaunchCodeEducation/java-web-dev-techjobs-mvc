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
            aValue = getFieldValue(job, field);

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
     * @param value Value of the field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<Job> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        ArrayList<Job> jobs = new ArrayList<>();

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
                break;
            } else if (job.getEmployer().toString().contains(value.toLowerCase())) {
                jobs.add(job);
                break;
            } else if (job.getLocation().toString().contains(value.toLowerCase())) {
                jobs.add(job);
                break;
            } else if (job.getPositionType().toString().contains(value.toLowerCase())) {
                jobs.add(job);
                break;
            } else if (job.getCoreCompetency().toString().contains(value.toLowerCase())) {
                jobs.add(job);
                break;
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

                Employer anEmployer = new Employer(headers[1]);
                if (!allEmployers.contains(anEmployer)){
                    allEmployers.add(anEmployer);
                }
                Location aLocation = new Location(headers[2]);
                if (!allLocations.contains(aLocation)){
                    allLocations.add(aLocation);
                }
                PositionType aPosition = new PositionType(headers[3]);
                if (!allPositionTypes.contains(aPosition)){
                    allPositionTypes.add(aPosition);
                }
                CoreCompetency aCoreCompetency = new CoreCompetency(headers[4]);
                if (!allCoreCompetency.contains(aCoreCompetency)){
                    allCoreCompetency.add(aCoreCompetency);
                }

                Job newJob = new Job(headers[0], anEmployer, aLocation, aPosition, aCoreCompetency);

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}

