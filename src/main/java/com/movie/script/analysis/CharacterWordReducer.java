package com.movie.script.analysis;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

public class CharacterWordReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    private Map<String, Integer> wordCountMap = new HashMap<>();

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0;
        for (IntWritable val : values) {
            sum += val.get();
        }
        wordCountMap.put(key.toString(), sum);
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(wordCountMap.entrySet());
        sortedEntries.sort((a, b) -> b.getValue().compareTo(a.getValue())); // Sort in descending order

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            context.write(new Text(entry.getKey()), new IntWritable(entry.getValue()));
        }
    }
}
