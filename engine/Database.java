package engine;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Database {
    private Map<String, List<Map<String, String>>> tableMap = new HashMap<>();

    public boolean createTable(String csvFilePath, String tableName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
            String[] header = reader.readLine().split(",");
            
            List<Map<String, String>> table = reader.lines()
                .map(line -> {
                    String[] values = line.split(",");
                    return IntStream.range(0, header.length)
                        .boxed()
                        .collect(Collectors.toMap(
                            i -> header[i],
                            i -> i < values.length ? values[i] : ""
                        ));
                }).collect(Collectors.toList());
            tableMap.put(tableName, table);
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

    public List<Map<String, String>> getTable(String tableName) {
        return tableMap.get(tableName);
    }

    public List<Map<String, String>> getJoinedTable(List<Map<String, String>> table1, List<Map<String, String>> table2, String commonField) {
        return table1.stream()
            .map(map1 -> {
                String commonValue = map1.get(commonField);
                Map<String, String> matchingMap2 = table2.stream()
                    .filter(map2 -> map2.get(commonField).equals(commonValue))
                    .findFirst()
                    .orElse(null);
                if (matchingMap2 != null) {
                    map1.putAll(matchingMap2);
                }
                return map1;
            }).collect(Collectors.toList());
    }

    public Map<String, List<Map<String, String>>> getGroupedTable(List<Map<String, String>> table, String groupByColumn) {
        Map<String, List<Map<String, String>>> result = new HashMap<>();
        for (Map<String,String> map : table) {
            String value = map.get(groupByColumn);
            if (!result.containsKey(value)) result.put(value, new ArrayList<>());
            result.get(value).add(map);
        }
        return result;
    }
}
