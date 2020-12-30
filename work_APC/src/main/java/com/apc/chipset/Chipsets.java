package com.apc.chipset;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import lombok.var;
import lombok.extern.slf4j.Slf4j;

//@Slf4j
class Chipsets {
	
	private static Map<Integer, List<List<Integer>>> solution = new HashMap<Integer, List<List<Integer>>>();

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Paths.get("/src/main/resources/input.txt"));

		// reading machines no
		int machinesNo = Integer.valueOf(lines.get(0));

		// reading chipsets list
		List<String> chipsetsList = Arrays.asList(lines.get(1).replace(" ", ",").split(","));
		List<Integer> chipsets = chipsetsList.stream().map(Integer::parseInt).collect(Collectors.toList());

		// reading number of chipsets to be produced
		int outputTarget = Integer.valueOf(lines.get(2));

		List<Integer> partialSolution = new ArrayList<>();
		find(chipsets, machinesNo, outputTarget, partialSolution);
		displaySolution(chipsets, outputTarget);
	}

	public static void find(List<Integer> chipsets, int machinesNo, int targetOutput, List<Integer> partialSolution) {

		// return if no machines were given
		if (targetOutput == 0) {
			// log.info("No machines existing.");
			return;
		}

		// return if no chipsets were given
		if (CollectionUtils.isEmpty(chipsets)) {
			// log.info("No chipsets defined. ");
			return;
		}

		List<List<Integer>> existingSolutions = solution.get(targetOutput);

		// init solution at first run
		if (CollectionUtils.isEmpty(existingSolutions) && CollectionUtils.isEmpty(partialSolution)) {
			existingSolutions = new ArrayList<List<Integer>>();
			solution.put(targetOutput, existingSolutions);
		}

		// adding a chipset to the partial solution
		for (Integer chipset : chipsets) {
			List<Integer> currSolution = new ArrayList<Integer>(partialSolution);
			currSolution.add(chipset);
			currSolution.sort(Integer::compare);

			// add a new solution based on each chipset
			List<Integer> chipsetSubList = new ArrayList<Integer>(chipsets);
			chipsetSubList.remove(chipset);
			List<Integer> auxPartialSolution = new ArrayList<Integer>(partialSolution);

			if (targetOutput < chipset) {
				// skip chipset from solution
				find(chipsetSubList, machinesNo, targetOutput, partialSolution);
				continue;
			}
			if (targetOutput > chipset) {
				// add chipset as solution
				auxPartialSolution.add(chipset);
				find(chipsetSubList, machinesNo, targetOutput - chipset, auxPartialSolution);
				continue;
			}
			if (targetOutput == chipset) {
				// last chipset to be added
				addSolution(currSolution, 0);
				continue;
			}

		}

	}

	private static void addSolution(List<Integer> sol, int partialResult) {
		int result = sol.stream().mapToInt(chipset -> chipset.intValue()).sum() + partialResult;
		List<List<Integer>> existingSolutions = solution.get(result);
		if (!existingSolutions.contains(sol)) {
			existingSolutions.add(sol);
			solution.put(result, existingSolutions);
		}
	}

	private static void displaySolution(List<Integer> allChipsets, int machinesNo) throws IOException {
		FileWriter fileWriter = new FileWriter("/src/main/resources/output.txt");
		PrintWriter printWriter = new PrintWriter(fileWriter);

		solution.forEach((key, value) -> {
			printWriter.println("Nr solutions = " + value.size());

			for (var val : value) {
				printWriter.println(val.toString() + " ");
			}
		});

		Set<Integer> usedChipsets = solution.get(machinesNo).stream().flatMap(list -> list.stream()).collect(Collectors.toSet());
		Set<Integer> wasteChipsets = new HashSet<>(allChipsets);
		wasteChipsets.removeAll(usedChipsets);
		printWriter.println("Waste = " + wasteChipsets.size());
		printWriter.close();
	}

}