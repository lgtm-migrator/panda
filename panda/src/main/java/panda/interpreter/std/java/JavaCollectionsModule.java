/*
 * Copyright (c) 2021 dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package panda.interpreter.std.java;

import panda.interpreter.resource.Mappings;

@Mappings(pkg = "java", author = "panda", module = "collections", commonPackage = "java.util", classes = {
        "Collection",
        "Collections",
        "Comparator",
        "Deque",
        "Iterator",
        "List",
        "Map",
        "Map$Entry",

        "ArrayDeque",
        "ArrayList",
        "HashMap",
        "HashSet",
        "Hashtable",
        "IdentityHashMap",
        "LinkedHashMap",
        "LinkedHashSet",
        "LinkedList",
        "Stack",
        "TreeMap",
        "TreeSet",
        "Vector",
        "WeakHashMap"
})
public final class JavaCollectionsModule { }
