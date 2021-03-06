/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.optaplanner.training.workerrostering.domain;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Employee {

    private final String name;
    private final Double time;
    // VIPs work less ;)
    private final Double vipFactor;
    private final Set<Skill> skillSet;
    private Boolean canDoND;
    private Boolean canDoFD;

    private Double expectedHours;
    private Set<TimeSlot> unavailableTimeSlotSet;
    private Set<TimeSlot> undesirableTimeSlotSet;
    private Set<TimeSlot> beforeVacationTimeSlotSet;
    private Set<TimeSlot> afterVacationTimeSlotSet;

    private Set<LocalDate> unavailableDateSet;
    private Set<LocalDate> undesirableDateSet;

    public Employee(String name, Set<Skill> skillSet, Double time, Double vipFactor) {
        this.name = name;
        this.skillSet = skillSet;
        //this.time = time;
        this.vipFactor = vipFactor;
        this.time = time * (1-vipFactor/12);
        init();
    }
    public Employee(String name, Set<Skill> skillSet) {
        this.name = name;
        this.skillSet = skillSet;
        this.time = 100.0;
        this.vipFactor = 0.0;
        init();
    }

    private void init() {
    	this.canDoND = getCanDoND();
    	this.canDoFD = getCanDoFD();
    	this.unavailableDateSet = new LinkedHashSet<>();
    	this.undesirableDateSet = new LinkedHashSet<>();
    	this.unavailableTimeSlotSet = new LinkedHashSet<>();
    	this.beforeVacationTimeSlotSet = new LinkedHashSet<>();
    	this.afterVacationTimeSlotSet = new LinkedHashSet<>();
    	this.undesirableTimeSlotSet = new LinkedHashSet<>();
    }

    public int getShiftUndesirableOverlap(ShiftAssignment sa) {
    	int overlap = 0;
    	for (LocalDate shiftDay : sa.getDays()) {
    		if (this.undesirableDateSet.contains(shiftDay)) {
    			++overlap;
    		}
    	}
    	return overlap;
    }

    public int getShiftVacationOverlap(ShiftAssignment sa) {
    	int overlap = 0;
    	for (LocalDate shiftDay : sa.getDays()) {
    		if (this.unavailableDateSet.contains(shiftDay)) {
    			++overlap;
    		}
    	}
    	return overlap;
    }

    public double getVIPFactor() {
        return vipFactor;
    }
    
    public double getDeviation(Number real) {
    	return getExpectedHours() - real.doubleValue();
    }
    public void setExpectedHours(double expectedHours) {
    	this.expectedHours = expectedHours;
    }
    
    public double getExpectedHours() {
    	return expectedHours;
    }

    public double getTime() {
        return time;
    }

    public double getTimeAdjustedCost(double cost) {
    	return cost * 100.0 / time;
    }
    public String getName() {
        return name;
    }

    public String getInfo() {
        String info = name + " |";
        info += getSkillsText();
        info += time + "|";
        info += vipFactor + "|";
        return info;
    }

	public String getSkillsText() {
		String text = "";
		for (Skill s : getSkillSet()) {
        	text += s + "|";
        }
		return text;
	}
    public Set<Skill> getSkillSet() {
        return skillSet;
    }

    public Set<TimeSlot> getUndesirableTimeSlotSet() {
        return undesirableTimeSlotSet;
    }

    public Set<TimeSlot> getBeforeVacationTimeSlotSet() {
        return beforeVacationTimeSlotSet;
    }

    public Set<TimeSlot> getAfterVacationTimeSlotSet() {
        return afterVacationTimeSlotSet;
    }

    public Set<TimeSlot> getUnavailableTimeSlotSet() {
        return unavailableTimeSlotSet;
    }

    public Set<LocalDate> getUndesirableDateSet() {
        return undesirableDateSet;
    }

    public Set<LocalDate> getUnavailableDateSet() {
        return unavailableDateSet;
    }

    public void setUnavailableTimeSlotSet(Set<TimeSlot> unavailableTimeSlotSet) {
        this.unavailableTimeSlotSet = unavailableTimeSlotSet;
    }

    public void setBeforeVacationTimeSlotSet(Set<TimeSlot> beforeVacationTimeSlotSet) {
        this.beforeVacationTimeSlotSet = beforeVacationTimeSlotSet;
    }

    public void setUndesirableTimeSlotSet(Set<TimeSlot> undesirableTimeSlotSet) {
        this.undesirableTimeSlotSet = undesirableTimeSlotSet;
    }

    public Boolean getHasSkill(Skill skill) {
    	return getSkillSet().contains(skill);
    }
    
    public Boolean getCanDoND() {
    	return getSkillSet().stream().anyMatch(s -> s.getName().equals("Night"));
    }

    public Boolean getCanDoFD() {
    	//return getSkillSet().stream().anyMatch(s -> s.getName().equals("Ro") || s.getName().equals("CT"));
    	return true;
    }

    public Boolean getCanDoJob(Spot spot) {
    	Skill requiredSkill = spot.getRequiredSkill();
    	Boolean hasRequiredSkill = 
    	 ("any".equals(requiredSkill.toString())) || getHasSkill(requiredSkill);

    	return hasRequiredSkill && !getHasSkill(spot.getUnsuitableSkill());
    }

    @Override
    public String toString() {
        return name;
    }

}
