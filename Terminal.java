package main;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;

public class Terminal {
	private String currentDirectory = System.getProperty("user.dir");

	/**
	 * Determines which command to execute based on the user's input and calls the
	 * corresponding method.
	 *
	 * @param command The main command extracted from the user input.
	 * @param args    A list of additional arguments provided with the command.
	 */
	public void chooseCommandAction(String command, ArrayList<String> args) {
		switch (command) {
			case "echo":
				echo(args);
				break;

			case "pwd":
				pwd();
				break;

			case "cd":
				cd(args);
				break;
				
			case "touch":
				touch(args.get(0));
				break;
				
			case "mkdir":
				if (args.size() > 0) {
					mkdir(args);
					break;
				}else {
					System.out.println("Please Enter an argument/arguments");
					break;
				}
				
			case "rmdir":
				rmdir(args.get(0));
				break;
				
			case "help":
				help();
				break;

			case "ls":
				ls(args);
				break;

			case "wc":
				wc(args);
				break;

			case"cp":
				cp(args);
				break;

			case "cp-r":
				if (args.size() != 2) {
					System.out.println("Usage: cp-r source_directory destination_directory");
				} else {

					copyDirectory(args);
				}
				break;

			case"rm":
				rm(args);
				break;

			case"cat":
				cat(args);
				break;

			default:
				System.out.println("Command not recognized. Type 'help' for available commands.");
		}
	}

	// Nour
	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------

	/**
	 * Implements the 'echo' command, which combines and prints the provided
	 * arguments.
	 *
	 * @param args A list of arguments to be combined and printed.
	 */

	public void echo(ArrayList<String> args) {
		String output = String.join(" ", args);
		System.out.println(output);
	}

	/*
	 * touch method
	 * takes 1 argument as a path and create a new file object
	 */
	public boolean touch(String arg) {

		try {
			Paths.get(arg);
		} catch (InvalidPathException e) {
			System.out.println("Wrong path");
			return false;
		}
		Path path = Paths.get(arg);
		path = path.toAbsolutePath();

		File new_file = new File(path.toString());
		try {
			if (new_file.createNewFile()) {
				System.out.println("File created: " + new_file.getName());
			} else {
				System.out.println("File already exist");
			}
		} catch (IOException e) {
			System.out.println("Error!");
			e.printStackTrace();
		}

		return true;
	}

	public void mkdir(ArrayList<String> args) {
		String mystr = currentDirectory.toString();
		for (String arg : args) {
			mystr += "\\" + arg;
		}
		File file = new File(mystr);
		if (!file.exists()) {
			file.mkdirs();
		} else {
			System.out.println("directory already exists");
		}
	}

	// Add more methods for other commands here
	public void rmdir(String arg) {
		if (arg.equals("*")) {
			File file = new File(currentDirectory);
			for (File subFile : file.listFiles()) {

				if (subFile.isDirectory() && subFile.length() == 0) {
					subFile.delete();
				}
			}
		} else {
			try {
				Paths.get(arg);
			} catch (InvalidPathException e) {
				System.out.println("no such file or directory");
				System.out.println(arg);
				return;
			}

			Path path = Paths.get(arg);
			path = path.toAbsolutePath();

			File file = new File(path.toString());
			if (file.length() == 0 && file.isDirectory()) {
				file.delete();
			}
		}
	}

	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------
	// Saleh
	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------



	public void cat(ArrayList<String> args) {
		for (String arg : args){
		try (Scanner input = new Scanner(Paths.get(args.get(0)))) {
			while (input.hasNextLine()) {
				System.out.println(input.nextLine());
			}
		} catch (IOException e) {
			System.err.println("An error occurred: " + e.getMessage());
		}
	}}
	public void rm(ArrayList<String>arg) {
		File myObj = Path.of(arg.get(0)).toFile();
		if (myObj.delete()) {
			System.out.println("Deleted the file: " + myObj.getName());
		} else {
			System.out.println("Failed to delete the file.");
		}

	}

	public void copyDirectory(ArrayList<String> args) {
		try {
			String sourceDirectory = args.get(0);
			String destinationDirectory = args.get(1);
			Files.copy(Path.of(sourceDirectory), Path.of(destinationDirectory));
			System.out.println("Directory copied successfully.");
		} catch (IOException e) {
			System.err.println("An error occurred: " + e.getMessage());
		}
	}

	public void cp(ArrayList<String> args){
		if(args.size()!=2){
			System.err.println("Usage :cp source destination");
			return;
		}
		String source = args.get(0);
		String destination = args.get(1);
		Path sourcePath = Path.of(source);
		Path destinationPath = Path.of(destination);

		try {
			Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
			System.out.println("File copied successfully.");
		} catch (IOException e) {
			System.err.println("An error occurred: " + e.getMessage());
		}



	}

	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------
	// JOHN
	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------
	/**
	 * Implements the 'pwd' command to print the current working directory to the
	 * console.
	 */
	public void pwd() {
		System.out.println(currentDirectory);

	}

	/**
	 * Implements the 'cd' command to change the current directory.
	 *
	 * @param args A list of additional arguments provided with the command.
	 */
	public void cd(ArrayList<String> args) {
		if (args.isEmpty())
		{
			// Case 1: cd with no arguments
			// Change the current directory to the user's home directory.
			currentDirectory = System.getProperty("user.home");
		}
		else if (args.size() == 1)  //there is a path or ..
		{
			if (args.get(0).equals("..")) {
				// Case 2: cd with ".." argument
				// Change the current directory to the parent directory.
				File currentDir = new File(currentDirectory);
				File parentDir = currentDir.getParentFile();
				if (parentDir != null)
				{
					currentDirectory = parentDir.getAbsolutePath();
				}
				else
				{
					System.out.println("Already in the root directory.");
				}

			}
			else
			{
				// Case 3: cd with a path argument
				// Change the current directory to the specified path (relative or full path).
				File newDir = new File(args.get(0));
				if (newDir.isAbsolute())
				{
					// Full path provided
					if (newDir.isDirectory())
					{
						currentDirectory = newDir.getAbsolutePath();
					}
					else
					{
						System.out.println("Invalid directory path: " + args.get(0));
					}
				}
				else
				{
					// Relative (short) path provided
					File updatedDir = new File(currentDirectory, args.get(0)); // add the relative pas to
																				// currentDirectory to get the absolute
																				// pass
					if (updatedDir.isDirectory())
					{
						currentDirectory = updatedDir.getAbsolutePath();
					}
					else
					{
						System.out.println("Invalid directory path: " + args.get(0));
					}
				}
			}
		}
		else
		{
			// Invalid number of arguments for the cd command
			System.out.println("Invalid number of arguments          Usage: cd [directory]");
		}
	}


	/**
	 * Implements the 'ls' command to list the contents of the current directory
	 * sorted alphabetically or in reverse order based on the presence of the "-r"
	 * flag.
	 *
	 * @param args A list of additional arguments provided with the command.
	 */
	public void ls(ArrayList<String> args) {
		File currentDir = new File(currentDirectory);

		if (!currentDir.isDirectory())
		{
			System.out.println("Invalid directory path: " + currentDirectory);
			return;
		}

		// Check for additional arguments after "ls"
		if (args.size() > 0 && !args.get(0).equals("-r"))
		{
			System.out.println("Invalid argument: " + args.get(0));
			return;
		}

		File[] files = currentDir.listFiles();

		if (args.size() == 1 && args.get(0).equals("-r"))
		{
			// Sort the list alphabetically in reverse order
			Arrays.sort(files, Collections.reverseOrder());
		}
		else
		{
			// Sort the list alphabetically
			Arrays.sort(files);
		}

		// Print the sorted list to the console
		for (File file : files)
		{
			System.out.println(file.getName());
		}
	}

	/**
	 * Implements the 'wc' command to count lines, words, and characters in a file.
	 *
	 * @param args The list of arguments, where the first argument is the file name.
	 */
	public void wc(ArrayList<String> args)
	{
		// Check if there is exactly one argument (the file name)
		if (args.size() != 1)
		{
			System.out.println("Usage: wc <file>");
			return;
		}

		// Get the file name from the arguments
		String fileName = args.get(0);

		// Build the full file path based on the current directory
		String filePath = currentDirectory + File.separator + fileName; // File.separator is the '/' symbol

		try {
			// Create a File object to represent the file
			File file = new File(filePath);

			// Check if the file exists and is a regular file
			if (!file.exists() || !file.isFile())
			{
				System.out.println("File not found: " + fileName);
				return;
			}

			// Initialize counters for line, word, and character counts
			int lineCount = 0;
			int wordCount = 0;
			int charCount = 0;

			// Create a scanner to read the file
			Scanner scanner = new Scanner(file);

			// Read the file line by line
			while (scanner.hasNextLine())
			{
				String line = scanner.nextLine();
				lineCount++;
				charCount += line.length();
				String[] words = line.split("\\s+"); // get each word alone separated by a space and added to words
														// array
				wordCount += words.length;
			}

			// Close the scanner
			scanner.close();

			// Print the line, word, character counts, and the file name
			System.out.println(lineCount + " " + wordCount + " " + charCount + " " + fileName);
		}
		catch (FileNotFoundException e)
		{
			// Handle any errors that may occur during file processing
			System.out.println("Error: " + e.getMessage());
		}
	}

	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------
	// ---------------------------------------------------------------------------------------------


	/**
	 * Displays a list of available commands and their usage to help the user.
	 */
	public void help() {
		System.out.println("Available commands:");
		System.out.println("echo <text>     	  ----> Prints the provided text.");
		System.out.println("pwd             	  ----> Prints the current directory path.");
		System.out.println("cd <directory>        ----> Change the current directory");
		System.out.println("ls [-r]         	  ----> Lists directory contents alphabetically or in reverse order.");
		System.out.println("wc <file>        	  ----> Count lines, words, and characters in a file");
		System.out.println("touch <file>    	  ----> Create a new file");
		System.out.println("mkdir <directory> 	  ----> Create a new directory");
		System.out.println("rmdir <directory> 	  ----> Remove an empty directory");
		System.out.println("cp <file> <file>      ----> Copy the first file into the second one");
		System.out.println("rm <file>        	  ----> removes this file.");
		System.out.println("cat <file> <file>     ----> 1 argument and prints the file’s content \n " +
				"        	          ---->or takes 2 arguments and concatenates the content of the 2 files and prints it.");

		System.out.println("\nexit               	  ----> Exits the program.");

		// List other available commands and their usage
	}
}
