package org.pz.filededupe.interfaces;

import org.pz.filededupe.LongStringListTable;

import java.util.List;
import java.util.function.Consumer;

/**
 * Prints duplicates and count to standard output
 *
 * @author dsaraiva
 */
public class StdOutput extends DupesOutput {

    @Override
    public void output(LongStringListTable dupes) {
        super.output(dupes, getDupesConsumer(), getFinalCountConsumer());
    }

    private Consumer<List<String>> getDupesConsumer() {
        return paths -> {
            paths.sort(null);
            System.out.println( "These files are the same:");
            for( String filepath : paths) {
                System.out.println( "\t" + filepath );
            }
            System.out.println();
        };
    }

    private Consumer<Integer> getFinalCountConsumer() {
        return dupesCount -> System.out.println( "Number of duplicates found: " + dupesCount );
    }
}