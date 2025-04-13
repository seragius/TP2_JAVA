package simulator.launcher;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.*;
import simulator.model.*;

public class Main {

    private static String _inFile = null;
    private static String _outFile = null;
    private static Factory<Event> _eventsFactory = null;
    private static int _timeLimit = 300;  // Default value if -t argument is not provided

    private static void parseArgs(String[] args) {
        Options cmdLineOptions = buildOptions();

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(cmdLineOptions, args);
            parseHelpOption(line, cmdLineOptions);
            parseInFileOption(line);
            parseOutFileOption(line);
            parseTimeLimitOption(line);
        } catch (ParseException e) {
            System.err.println(e.getLocalizedMessage());
            System.exit(1);
        }
    }

    private static Options buildOptions() {
        Options cmdLineOptions = new Options();

        cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
        cmdLineOptions.addOption(Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
        cmdLineOptions.addOption(Option.builder("t").longOpt("timeLimit").hasArg().desc("Number of simulation steps").build());
        cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());

        return cmdLineOptions;
    }

    private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
        if (line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
            System.exit(0);
        }
    }

    private static void parseInFileOption(CommandLine line) throws ParseException {
        _inFile = line.getOptionValue("i");
        if (_inFile == null) {
            throw new ParseException("An events file is missing");
        }
    }

    private static void parseOutFileOption(CommandLine line) throws ParseException {
        _outFile = line.getOptionValue("o");
    }

    private static void parseTimeLimitOption(CommandLine line) {
        if (line.hasOption("t")) {
            _timeLimit = Integer.parseInt(line.getOptionValue("t"));
        }
    }

    private static void initFactories() {
        // Light Switching Strategies
        List<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
        lsbs.add(new RoundRobinStrategyBuilder());
        lsbs.add(new MostCrowdedStrategyBuilder());
        Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);

        // Dequeuing Strategies
        List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
        dqbs.add(new MoveFirstStrategyBuilder());
        dqbs.add(new MoveAllStrategyBuilder());
        Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);

        // Event Builders
        List<Builder<Event>> eventBuilders = new ArrayList<>();
        eventBuilders.add(new NewJunctionEventBuilder(lssFactory, dqsFactory));
        eventBuilders.add(new NewCityRoadEventBuilder());
        eventBuilders.add(new NewInterCityRoadEventBuilder());
        eventBuilders.add(new NewVehicleEventBuilder());
        eventBuilders.add(new SetWeatherEventBuilder());
        eventBuilders.add(new SetContClassEventBuilder());

        _eventsFactory = new BuilderBasedFactory<>(eventBuilders);
    }


    private static void startBatchMode() throws IOException {
        // Create InputStream and OutputStream
        InputStream in = new FileInputStream(_inFile);
        OutputStream out = (_outFile == null) ? System.out : new FileOutputStream(_outFile);

        // Create simulator and controller
        TrafficSimulator simulator = new TrafficSimulator();
        Controller controller = new Controller(simulator, _eventsFactory);

        // Load events and run the simulation
        controller.loadEvents(in);
        in.close();
        controller.run(_timeLimit, out);
    }

    public static void start(String[] args) throws IOException {
        initFactories();
        parseArgs(args);
        startBatchMode();
    }

    public static void main(String[] args) {
        try {
            start(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
