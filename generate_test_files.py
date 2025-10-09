#!/usr/bin/env python3

def generate_map_of_entries_test(num_items=100):
    """Generate MapOfEntriesTest.java with specified number of items"""
    
    entries = []
    for i in range(1, num_items + 1):
        entries.append(f'        Map.entry("key{i}", "value{i}")')
    
    entries_str = ',\n'.join(entries)
    
    content = f"""package com.kanatti.compile;

import java.util.LinkedHashMap;
import java.util.Map;

public class MapOfEntriesTest {{
    private static final LinkedHashMap<String, String> DATA = new LinkedHashMap<>(Map.ofEntries(
{entries_str}
    ));

    public static void main(String[] args) {{
        System.out.println("MapOfEntries test loaded with " + DATA.size() + " entries");
    }}
}}"""
    
    return content

def generate_put_method_test(num_items=100):
    """Generate PutMethodTest.java with specified number of items"""
    
    puts = []
    for i in range(1, num_items + 1):
        puts.append(f'        DATA.put("key{i}", "value{i}");')
    
    puts_str = '\n'.join(puts)
    
    content = f"""package com.kanatti.compile;

import java.util.LinkedHashMap;

public class PutMethodTest {{
    private static final LinkedHashMap<String, String> DATA = new LinkedHashMap<>();
    
    static {{
{puts_str}
    }}

    public static void main(String[] args) {{
        System.out.println("PutMethod test loaded with " + DATA.size() + " entries");
    }}
}}"""
    
    return content

def main():
    import sys
    
    # Default to 100 items, but allow override from command line
    num_items = 100
    if len(sys.argv) > 1:
        try:
            num_items = int(sys.argv[1])
        except ValueError:
            print(f"Invalid number: {sys.argv[1]}. Using default: 100")
    
    # Generate MapOfEntriesTest
    map_content = generate_map_of_entries_test(num_items)
    map_path = "src/mapOfEntries/java/com/kanatti/compile/MapOfEntriesTest.java"
    with open(map_path, 'w') as f:
        f.write(map_content)
    print(f"Generated {map_path} with {num_items} items")
    
    # Generate PutMethodTest
    put_content = generate_put_method_test(num_items)
    put_path = "src/putMethod/java/com/kanatti/compile/PutMethodTest.java"
    with open(put_path, 'w') as f:
        f.write(put_content)
    print(f"Generated {put_path} with {num_items} items")

if __name__ == "__main__":
    main()
