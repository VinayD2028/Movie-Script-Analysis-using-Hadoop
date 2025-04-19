package com.movie.script.analysis;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class UniqueWordsMapper extends Mapper<Object, Text, Text, Text> {

    private Text character = new Text();
    private Text word = new Text();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString().trim();
        if (!line.contains(":")) return;

        String[] parts = line.split(":", 2);
        if (parts.length < 2) return;

        character.set(parts[0].trim());
        String dialogue = parts[1].trim();

        StringTokenizer tokenizer = new StringTokenizer(dialogue);
        while (tokenizer.hasMoreTokens()) {
            word.set(tokenizer.nextToken().replaceAll("[^a-zA-Z]", "").toLowerCase());
            if (!word.toString().isEmpty()) {
                context.write(character, word);
            }
        }
    }
}
