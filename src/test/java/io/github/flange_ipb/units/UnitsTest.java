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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.measure.MetricPrefix;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import javax.measure.quantity.AmountOfSubstance;
import javax.measure.quantity.Mass;
import javax.measure.quantity.Volume;

import org.junit.jupiter.api.Test;

import tech.units.indriya.unit.Units;

public class UnitsTest {
	@Test
	public void test_massUnits() {
		Unit<Mass> g1 = Units.GRAM;
		Unit<Mass> kg1 = Units.KILOGRAM;

		assertEquals(null, g1.getName());
		assertEquals("Kilogram", kg1.getName());

		assertEquals(null, g1.getSymbol()); // Are you kidding me?!?
		assertEquals("kg", kg1.getSymbol());

		assertEquals("g", g1.toString()); // Magic?
		assertEquals("kg", kg1.toString());

		Unit<Mass> g2 = kg1.divide(1000);
		assertEquals(g1, g2);
		assertEquals("g", g2.toString());

		Unit<Mass> kg2 = g2.multiply(1000);
		assertNotEquals(kg2, kg1); // Einstein's brain is turning!
		assertEquals("g*1000", kg2.toString());

		Unit<Mass> g3 = MetricPrefix.MILLI(kg1);
		assertNotEquals(g1, g3); // It's turning again ...
		assertEquals("mkg", g3.toString()); // epic fail

		// something better than Object.equals()
		assertFalse(kg1.isEquivalentTo(g1));
		assertFalse(g1.isEquivalentTo(kg1));
		assertTrue(g1.isEquivalentTo(g2));
		assertTrue(g2.isEquivalentTo(g1));
		assertTrue(kg1.isEquivalentTo(kg2)); // Einstein's brain is pleased.
		assertTrue(kg2.isEquivalentTo(kg1));
		assertTrue(g1.isEquivalentTo(g3));
		assertTrue(g3.isEquivalentTo(g1));
		assertTrue(g2.isEquivalentTo(g3));
		assertTrue(g3.isEquivalentTo(g2));
	}

	@Test
	public void test_concentrations() {
		Unit<AmountOfSubstance> mole = Units.MOLE;
		assertEquals("Mole", mole.getName());
		assertEquals("mol", mole.getSymbol());
		assertEquals("mol", mole.toString());

		Unit<Volume> liter = Units.LITRE;
		assertEquals("Litre", liter.getName());
		assertEquals("l", liter.getSymbol());
		assertEquals("l", liter.toString());

		Unit<AmountOfSubstance> milliMole = MetricPrefix.MILLI(Units.MOLE);
		assertEquals(null, milliMole.getName());
		assertEquals(null, milliMole.getSymbol());
		assertEquals("mmol", milliMole.toString());

		Unit<Molarity> molePerLiter = mole.divide(liter).asType(Molarity.class);
		assertEquals(null, molePerLiter.getName());
		assertEquals(null, molePerLiter.getSymbol());
		assertEquals("mol/l", molePerLiter.toString());

		Unit<Molarity> milliMolePerLiter = milliMole.divide(liter).asType(Molarity.class);
		assertEquals(null, milliMolePerLiter.getName());
		assertEquals(null, milliMolePerLiter.getSymbol());
		assertEquals("mmol/l", milliMolePerLiter.toString());

		UnitConverter conv = molePerLiter.getConverterTo(milliMolePerLiter);
		assertEquals(1000, conv.convert(1));

		Unit<Volume> milliLiter = MetricPrefix.MILLI(Units.LITRE);
		Unit<AmountOfSubstance> milliMole2 = molePerLiter.multiply(milliLiter).asType(AmountOfSubstance.class);
		assertTrue(milliMole.isEquivalentTo(milliMole2));
		Unit<?> milliMole3 = molePerLiter.multiply(milliLiter);
		assertTrue(milliMole.isCompatible(milliMole3));
	}
}