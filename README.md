
# Movie Script Analysis

This project implements a Hadoop MapReduce program to analyze movie script dialogues. The program processes a dataset where each line represents a movie dialogue in the format:
```bash
Character: Dialogue
```

## Approach and implementation
The project consists of three main MapReduce jobs:
1. Most Frequently Spoken Words by Characters
Mapper (CharacterWordMapper.java): Splits each line into Character and Dialogue, Tokenizes the dialogue into words, Emits (Character:Word, 1) key-value pairs.
Reducer (CharacterWordReducer.java):Aggregates word counts per character, Outputs the frequency of each word spoken by characters.

2. Total Dialogue Length per Character
Mapper (DialogueLengthMapper.java):Extracts the character name and calculates the length of their dialogue, Emits (Character, dialogue length) key-value pairs.
Reducer (DialogueLengthReducer.java):Sums up the total dialogue length per character.

3. Unique Words Used by Each Character
Mapper (UniqueWordsMapper.java):Tokenizes the dialogue and emits (Character, Word) key-value pairs.
Reducer (UniqueWordsReducer.java):Collects unique words spoken by each character.

Additionally, Hadoop Counters track:
Total number of lines processed
Total words processed
Total characters processed
Total unique words identified
Number of characters speaking

## Setup and Execution

### 1. **Start the Hadoop Cluster**

Run the following command to start the Hadoop cluster:

```bash
docker compose up -d
```

### 2. **Build the Code**

Build the code using Maven:

```bash
mvn clean package
```

### 4. **Copy JAR to Docker Container**

Copy the JAR file to the Hadoop ResourceManager container:

```bash
docker cp target/hands-on2-movie-script-analysis-1.0-SNAPSHOT.jar resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 5. **Move Dataset to Docker Container**

Copy the dataset to the Hadoop ResourceManager container:

```bash
docker cp input/movie_dialogues.txt resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 6. **Connect to Docker Container**

Access the Hadoop ResourceManager container:

```bash
docker exec -it resourcemanager /bin/bash
```

Navigate to the Hadoop directory:

```bash
cd /opt/hadoop-3.2.1/share/hadoop/mapreduce/
```

### 7. **Set Up HDFS**

Create a folder in HDFS for the input dataset:

```bash
hadoop fs -mkdir -p /input/movie_scripts
```

Copy the input dataset to the HDFS folder:

```bash
hadoop fs -put movie_dialogues.txt /input/movie_scripts/
```

### 8. **Execute the MapReduce Job**

Run your MapReduce job using the following command: Here I got an error saying output already exists so I changed it to output2 instead as destination folder

```bash
hadoop jar hands-on2-movie-script-analysis-1.0-SNAPSHOT.jar com.movie.script.analysis.MovieScriptAnalysis /input/movie_scripts/movie_dialogues.txt /output2
```

### 9. **View the Output**

To view the output of your MapReduce job, use:

```bash
hadoop fs -ls /output2
```
9.2 View the Output Files for Each Task
Task 1: Most Frequent Words by Character
```bash
hadoop fs -cat /output2/task1/part-r-00000
```
Task 2: Dialogue Length Analysis
```bash
hadoop fs -cat /output2/task2/part-r-00000
```
Task 3: Unique Words by Character
```bash
hadoop fs -cat /output2/task3/part-r-00000
```
These commands will display the results for each analysis task.

### 10. **Copy Output from HDFS to Local OS**
To copy the output from HDFS to your local machine:
1. Use the following command to copy from HDFS:
    ```bash
    hadoop fs -get /output2 /opt/hadoop-3.2.1/share/hadoop/mapreduce/
    ```
2. use Docker to copy from the container to your local machine:
   ```bash
   exit 
   ```
    ```bash
    docker cp resourcemanager:/opt/hadoop-3.2.1/share/hadoop/mapreduce/output2/ ./output2/
    ```
3. Commit and push to your repo so that we can able to see your output

### 11. Submit Your Code and Output
11.1 Push Your Code and Output to GitHub
Commit your changes, including the output from the MapReduce job, and push them to your GitHub repository:
```bash
git add .
git commit -m "Completed Movie Script Analysis Assignment"
git push origin main
```

## Challenges faced: 
 1. Handling Special Characters in Dialogue: Used regex to clean non-alphabetical characters from words.
 2. Counting Unique Words Efficiently: Used a HashSet in the reducer to avoid duplicates.

## Sample Input: 
 ```bash
JACK: The ship is sinking! We have to go now.
ROSE: I won’t leave without you.
JACK: We don’t have time, Rose!
   ```

## Expected output: 
1. Most Frequently Spoken Words by Characters
 ```bash
the 3
we 3
have 2
to 2
now 1
without 1
   ```

2. Total Dialogue Length per Character
 ```bash
JACK 54
ROSE 25
   ```

3. Unique Words Used by Each Character
 ```bash
JACK [the, ship, is, sinking, we, have, to, go, now, don’t, time, rose]
ROSE [i, won’t, leave, without, you]
   ```
