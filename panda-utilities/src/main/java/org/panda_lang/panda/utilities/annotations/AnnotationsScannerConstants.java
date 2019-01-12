/*
 * Copyright (c) 2015-2019 Dzikoysk
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

package org.panda_lang.panda.utilities.annotations;

import java.util.Arrays;
import java.util.List;

class AnnotationsScannerConstants {

    static final String[] PANDA_PACKAGES = {
            "META-INF",
            "java", "com.sun", "sun", "jdk", "javax", "oracle", "com.oracle", "netscape",       // Java
            "org.apache", "com.google", "org.slf4j",                                            // Popular
            "org.junit", "junit", "org.opentest4j",                                             // Tests
            "org.jetbrains", "org.intellij", "com.intellij",                                    // IDE
            "javassist", "org.fusesource", "org.apiguardian"                                    // Internal
    };

    static List<String> primitiveNames = Arrays.asList("boolean", "char", "byte", "short", "int", "long", "float", "double", "void");

    static List<String> primitiveDescriptors = Arrays.asList("Z", "C", "B", "S", "I", "J", "F", "D", "V");

    static List<Class> primitiveTypes = Arrays.asList(boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class, void.class);

}
