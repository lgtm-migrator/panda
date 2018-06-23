/*
 * Copyright (c) 2015-2018 Dzikoysk
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

package org.panda_lang.panda.utilities.commons.objects;

import org.junit.jupiter.api.*;

public class CharacterUtilsTest {

    @Test
    public void testBelongsTo() {
        Assertions.assertTrue(CharacterUtils.belongsTo('b', "abc".toCharArray()));
        Assertions.assertFalse(CharacterUtils.belongsTo('d', "abc".toCharArray()));
    }

    @Test
    public void testIsWhitespace() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(CharacterUtils.isWhitespace(' ')),
                () -> Assertions.assertFalse(CharacterUtils.isWhitespace('a')),

                () -> Assertions.assertTrue(CharacterUtils.isWhitespace((char) 0)),
                () -> Assertions.assertTrue(CharacterUtils.isWhitespace(CharacterUtils.TAB)),
                () -> Assertions.assertTrue(CharacterUtils.isWhitespace(CharacterUtils.NO_BREAK_SPACE))
        );
    }

    @Test
    public void testGetIndex() {
        Assertions.assertEquals(-1, CharacterUtils.getIndex(new char[] { 'a', 'b', 'c'}, 'd'));
        Assertions.assertEquals(1, CharacterUtils.getIndex(new char[] { 'a', 'b', 'c'}, 'b'));
    }

}
