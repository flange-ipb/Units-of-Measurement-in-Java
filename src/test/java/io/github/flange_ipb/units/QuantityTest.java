/*
 * Copyright 2022 The Author
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
package io.github.flange_ipb.units;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.measure.MetricPrefix;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Speed;
import javax.measure.quantity.Time;

import org.junit.jupiter.api.Test;

import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.unit.Units;

public class QuantityTest {
	@Test
	public void test_comparisons() {
		ComparableQuantity<Mass> potatoes = Quantities.getQuantity(5, Units.KILOGRAM);
		ComparableQuantity<Mass> cherry = Quantities.getQuantity(20, Units.GRAM);
		ComparableQuantity<Mass> fiveThousandGrams = Quantities.getQuantity(5000, Units.GRAM);

		assertTrue(potatoes.isGreaterThan(cherry));
		assertFalse(cherry.isGreaterThan(potatoes));
		assertTrue(cherry.isLessThan(potatoes));
		assertFalse(potatoes.isLessThan(cherry));

		assertTrue(potatoes.isEquivalentTo(fiveThousandGrams));
		assertTrue(fiveThousandGrams.isEquivalentTo(potatoes));
		assertFalse(potatoes.isGreaterThan(fiveThousandGrams));
		assertFalse(potatoes.isLessThan(fiveThousandGrams));
		assertFalse(fiveThousandGrams.isGreaterThan(potatoes));
		assertFalse(fiveThousandGrams.isLessThan(potatoes));
		assertTrue(potatoes.isGreaterThanOrEqualTo(fiveThousandGrams));
		assertTrue(fiveThousandGrams.isGreaterThanOrEqualTo(potatoes));
		assertTrue(potatoes.isLessThanOrEqualTo(fiveThousandGrams));
		assertTrue(fiveThousandGrams.isLessThanOrEqualTo(potatoes));
	}

	@Test
	public void test_calculations() {
		Quantity<Length> distance = Quantities.getQuantity(680, Units.METRE);
		Quantity<Time> time = Quantities.getQuantity(2000, MetricPrefix.MILLI(Units.SECOND));

		Quantity<?> speedOfSound = distance.divide(time);
		assertEquals(0.34, speedOfSound.getValue().doubleValue());
		assertEquals("m/ms", speedOfSound.getUnit().toString());
		assertEquals("[L]/[T]", speedOfSound.getUnit().getDimension().toString());

		Unit<Speed> meterPerSecond = Units.METRE_PER_SECOND;
		assertTrue(speedOfSound.getUnit().isCompatible(meterPerSecond));

		Quantity<Speed> speedOfSoundInMeterPerSecond = speedOfSound.asType(Speed.class).to(meterPerSecond);
		assertEquals(340, speedOfSoundInMeterPerSecond.getValue().doubleValue());
		assertEquals("m/s", speedOfSoundInMeterPerSecond.getUnit().toString());
		assertEquals("[L]/[T]", speedOfSoundInMeterPerSecond.getUnit().getDimension().toString());
	}
}