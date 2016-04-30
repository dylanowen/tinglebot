package com.dylowen.tinglebot;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.dylowen.tinglebot.brain.TextBrain;
import com.dylowen.tinglebot.train.SerializedBrainTrainer;
import com.dylowen.tinglebot.train.SkypeDatabaseTrainer;
import com.dylowen.tinglebot.train.TextTrainer;
import com.dylowen.tinglebot.train.JavaTrainer;
import scala.collection.immutable.List;

/**
 * TODO add description
 *
 * @author dylan.owen
 * @since Feb-2016
 */
public class MainJava {

    final static Option BRAIN_EXPORT_PATH = Option.builder("e").longOpt("export").argName(
            "export_location").hasArg().desc("where to export the brain").build();

    final static Option HELP = new Option("help", "print this menu");

    final static Options MAIN_OPTIONS = new Options();

    static {
        MAIN_OPTIONS.addOption(BRAIN_EXPORT_PATH);
        MAIN_OPTIONS.addOption(HELP);
    }

    /*
    public static void main2(String[] args) {
        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine line = parser.parse(MAIN_OPTIONS, args, true);

            if (line.hasOption(HELP.getOpt())) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("tinglebot <trainer_file>", MAIN_OPTIONS, true);

                return;
            }

            final String path;
            final String exportPath = line.getOptionValue(BRAIN_EXPORT_PATH.getOpt());

            //make sure we got a training file
            if (line.getArgs().length > 0) {
                path = line.getArgs()[0];
            }
            else {
                System.err.println("Please enter a training file location");
                return;
            }

            final int extensionIndex = path.lastIndexOf('.');
            final String extension = (extensionIndex < 0) ? "" : path.substring(extensionIndex + 1);

            final JavaTrainer<TextBrain> trainer;
            if ("json".equals(extension)) {
                trainer = new SkypeDatabaseTrainer(path);
            }
            else if ("txt".equals(extension)) {
                trainer = new TextTrainer(path);
            }
            else if ("brain".equals(extension)) {
                trainer = new SerializedBrainTrainer<>(path);
            }
            else {
                throw new UnsupportedOperationException("Unknown file type");
            }

            final TextBrain brain = trainer.train();

            //check if we should export the brain
            /*
            if (exportPath != null) {
                brain.export(exportPath);
            }
            * /

            final Timer timer = new Timer();
            for (int i = 0; i < 10; i++) {
                final StringBuilder sb = new StringBuilder();

                List<String> sentence = brain.getSentenceWords();
                sb.append(brain.concatSentence(sentence));
                while (sb.length() < 140) {
                    sb.append("\n");
                    sentence = brain.getSentenceWords();
                    sb.append(brain.concatSentence(sentence));
                }
                System.out.println(sb.toString() + "\n");
            }
            System.out.println("sentence generation time: " + timer.getS() + "s");
        }
        catch (MissingOptionException e) {
            for (Object o : e.getMissingOptions()) {
                printMissingArgError(MAIN_OPTIONS.getOption(o.toString()));
            }
        }
        catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed. Reason: " + exp.getMessage());
        }
    }
    */

    static void printMissingArgError(final Option option) {
        System.err.println("Missing Argument: <" + option.getLongOpt() + "> " + option.getDescription());
    }
}
