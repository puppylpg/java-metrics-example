package example.java8.stream;

import example.java8.objs.Album;
import example.java8.objs.Artist;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 1. use collect(Collector) in Stream to generate a Collection;
 * 2. use methods in Collectors to generate Collector.
 *
 * Sure, there is another collect() in Stream.
 *
 * @author liuhaibo on 2017/11/23
 */
public class CollectAndCollectors {

    private static List<Integer> list = Arrays.asList(1, 1, null, 2, 3, 4, null, 5, 6, 7, 8, 9, 10);

    ///// Stream.collect(1) /////

    /**
     * Stream.collect(Supplier, BiConsumer, BiConsumer)
     */
    private static void overridedCollect() {

        // Replacement for constructor & method!!!
        // .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        List<Integer> listNonNull = list.stream().filter(Objects::nonNull).collect(
                () -> new ArrayList<Integer>(),
                (list, item) -> list.add(item),
                (list1, list2) -> list1.addAll(list2)
        );
    }

    ///// Stream.collect(2) /////

    /**
     * Collectors.toList()
     */
    private static void usingToList() {
        // 'x -> x != null'  equals  'Objects::nonNull'
        List<Integer> listNonNull = list.stream().filter(x -> x != null).collect(Collectors.toList());
    }

    /**
     * Collectors.toCollection(Supplier)
     */
    private static void usingToCollection() {
        List<Integer> listNonNull = list.stream().filter(Objects::nonNull).collect(
                Collectors.toCollection(LinkedList::new)
        );
    }

    /**
     * Collectors.maxBy(Comparator) = stream.max(Comparator)
     *
     * & new way to generate a Comparator in java8: just pass a key extractor.
     *
     * @param artists
     */
    private static void usingMaxBy(List<Artist> artists) {
        // a key extractor
        Function<Artist, Long> getNum = artist -> artist.getMembers().count();

        // a Comparator
        Comparator<Artist> comp = Comparator.comparing(getNum);

        // artist.stream().max(comp);
        Optional<Artist> biggestGroup = artists.stream().collect(
                Collectors.maxBy(comp)
        );
    }

    /**
     * Collectors.averagingInt(ToIntFunction)
     * @param albums
     */
    private static void usingAveragingInt(List<Album> albums) {
        double aveTrackNum = albums.stream().collect(
                Collectors.averagingInt(x -> x.getTrackList().size())
        );
    }

    /**
     * Collectors.partitioningBy(Predicate)
     *
     * @param artists
     */
    private static void usingPartitioningBy(List<Artist> artists) {
        Map<Boolean, List<Artist>> soloVsBand = artists.stream().collect(
                Collectors.partitioningBy(Artist::isSolo)
        );
    }

    /**
     * Collectors.groupingBy(Function)
     *
     * partitioningBy can only divide stream into 2 parts according TRUE/FALSE
     * groupingBy is like 'group by' in SQL, which can divide stream into many parts according to conditions
     *
     * @param artists
     */
    private static void usingGroupingBy(List<Artist> artists) {
        Map<String, List<Artist>> groupByNationality = artists.stream().collect(
                Collectors.groupingBy(Artist::getNationality)
        );
    }

    public static void main(String [] args) {

    }
}