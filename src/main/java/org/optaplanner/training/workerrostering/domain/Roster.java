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

import java.util.List;
import java.util.stream.Collectors;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.solver.Solver;

@PlanningSolution
public class Roster {

    @ProblemFactProperty
    private RosterParametrization rosterParametrization;
    @ProblemFactCollectionProperty
    private List<Skill> skillList;
    @ProblemFactCollectionProperty
    private List<Spot> spotList;
    @ProblemFactCollectionProperty
    private List<TimeSlot> timeSlotList;
    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "employeeRange")
    private List<Employee> employeeList;
    public Solver<Roster> Solver;

    @PlanningEntityCollectionProperty
    private List<ShiftAssignment> shiftAssignmentList;

    @PlanningScore
    private HardMediumSoftScore score = null;

    private Roster() {
    }

    public Roster(RosterParametrization rosterParametrization,
            List<Skill> skillList, List<Spot> spotList, List<TimeSlot> timeSlotList, List<Employee> employeeList,
            List<ShiftAssignment> shiftAssignmentList) {
        this.rosterParametrization = rosterParametrization;
        this.skillList = skillList;
        this.spotList = spotList;
        this.timeSlotList = timeSlotList;
        this.employeeList = employeeList;
        this.shiftAssignmentList = shiftAssignmentList;
        
        for (ShiftAssignment sa : this.shiftAssignmentList) {
        	sa.setRoster(this);
        }

        double expectedHoursForFullTime = getTotalDays() / getTotalEmployeeTime() * 100.0;
        for (Employee emp : this.employeeList) {
        	emp.setExpectedHours(expectedHoursForFullTime * emp.getTime() / 100.0);
        }
    }

    public RosterParametrization getRosterParametrization() {
        return rosterParametrization;
    }

    public List<Skill> getSkillList() {
        return skillList;
    }

    public List<Spot> getSpotList() {
        return spotList;
    }

    public List<TimeSlot> getTimeSlotList() {
        return timeSlotList;
    }

    public List<Employee> getEmployeeList() {
		List<Employee> sorted = employeeList.stream().sorted((a, b) -> a.getName().compareTo(b.getName()))
				.collect(Collectors.toList());
		return sorted;
    }

    public List<ShiftAssignment> getShiftAssignmentList() {
        return shiftAssignmentList;
    }
    
    public double getTotalDays() {
    	return shiftAssignmentList.stream().mapToDouble(s -> s.getSpot().getDays()).sum();
    }

    public double getTotalEmployeeTime() {
    	return employeeList.stream().mapToDouble(e -> e.getTime()).sum();
    }

    public List<ShiftAssignment> getEmployeeAssignments(Employee emp) {
    	return shiftAssignmentList.stream()
    			.filter(s -> s.getEmployee() != null && s.getEmployee().getName().equals(emp.getName()))
    			.collect(Collectors.toList());
    }
    public HardMediumSoftScore getScore() {
        return score;
    }

}
