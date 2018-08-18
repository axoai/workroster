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

public class Spot {

    private final String name;
    private final Skill requiredSkill;
    private final Skill unsuitableSkill;
    private final Double hours;
    private final int scoreBeforeVacation;

    public Spot(String name, Skill requiredSkill, Skill unsuitableSkill, Double hours, int scoreBeforeVacation) {
        this.name = name;
        this.requiredSkill = requiredSkill;
        this.unsuitableSkill = unsuitableSkill;
        this.hours = hours;
        this.scoreBeforeVacation = scoreBeforeVacation;
    }

    public Spot(String name, Skill requiredSkill) {
        this.name = name;
        this.requiredSkill = requiredSkill;
        this.unsuitableSkill = null;
        this.hours = 8.0;
        this.scoreBeforeVacation = 0;
    }

    public String getName() {
        return name;
    }

    public String getShiftType() {
        return name.split("_")[0];
    }

    public Skill getRequiredSkill() {
        return requiredSkill;
    }

    public int getScoreBeforeVacation() {
        return scoreBeforeVacation;
    }

    public Skill getUnsuitableSkill() {
        return unsuitableSkill;
    }

    public Double getHours() {
        return hours;
    }

    @Override
    public String toString() {
        return name;
    }

}
