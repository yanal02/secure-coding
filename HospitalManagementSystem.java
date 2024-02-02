package yanalbawab;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;


public class HospitalManagementSystem {
	private int loginAttempts;
	private static final int MAX_LOGIN_ATTEMPTS = 3;
	private String adminPasswordHash; // Hashed admin password
	private String adminUsername; // Admin username

	public HospitalManagementSystem() {
		loginAttempts = 0;

		// Load admin credentials from file (if needed)
		// loadAdminCredentials(adminUsername, adminPasswordHash);
	}

	public String getAdminPasswordHash() {
		return adminPasswordHash;
	}

	public void setAdminPasswordHash(String adminPasswordHash) {
		this.adminPasswordHash = adminPasswordHash;
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	private static boolean loadAdminCredentials(String adminUsername, String hashedPassword) {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(
					"C:\\Users\\Yanal\\eclipse-workspace\\yanalbawabsc\\admin_credentials.txt"));

			String line;
			while ((line = br.readLine()) != null) {
				String[] columns = line.split(",");
				if (columns.length >= 2 && columns[0].equalsIgnoreCase(adminUsername)
						&& columns[1].equals(hashedPassword)) {
					return true; // Valid admin credentials found in the file
				}
			}
		} catch (IOException e) {
			System.err.println("Error while reading admin credentials file");

		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				System.err.println("Error closing the reader");
			}
		}
		return false; // No match found or error occurred
	}

	private boolean isStrongPassword(String password) {
		return password.length() >= 8;
	}

	private static String getHash(String value) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			result = encode(md.digest(value.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			System.err.println("The Algorithm doesn't exist");
		}
		return result;
	}

	private static String encode(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}

	public boolean Userlogin(String username, String password, String userType) {
		if (loginAttempts >= MAX_LOGIN_ATTEMPTS) {
			System.err.println("Login attempts exceeded. Please try again later.");
			return false;
		}

		// Choose the appropriate file based on the userType
		String filePath = "";
		if ("Doctor".equalsIgnoreCase(userType)) {
			filePath = "C:\\Users\\Yanal\\eclipse-workspace\\yanalbawabsc\\Doctor.txt";
		} else if ("Patient".equalsIgnoreCase(userType)) {
			filePath = "C:\\Users\\Yanal\\eclipse-workspace\\yanalbawabsc\\Patient.txt";
		} else {
			System.err.println("Invalid user type.");
			return false;
		}

		// Read credentials from the chosen file
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] columns = line.split(",");
				if (columns.length >= 2 && columns[0].equalsIgnoreCase(username) && columns[1].equals(password)) {
					// Add logic here for doctor or patient specific actions
					return true;
				}
			}
		} catch (IOException e) {
			System.err.println("Error while reading credentials file: " + e.getMessage());
		}

		System.err.println("Login failed. Invalid username or password for " + userType + ".");
		loginAttempts++;
		return false;
	}

	public void adminMenu() {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			System.out.println("Admin Menu:");
			System.out.println("1. Register Doctor");
			System.out.println("2. Register Patient");
			System.out.println("3. Exit");
			System.out.print("Enter your choice (1/2/3): ");
			int adminChoice = scanner.nextInt();
			scanner.nextLine(); // Consume newline character

			switch (adminChoice) {
			case 1:
				registerUserForm("Doctor");
				break;
			case 2:
				registerUserForm("Patient");
				break;
			case 3:
				return; // Exit the admin menu
			default:
				System.err.println("Invalid choice.");
			}
		}
	}

	public void registerUserForm(String role) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Register " + role);
		System.out.print("Enter name: ");
		String name = scanner.nextLine();
		System.out.print("Enter password: ");
		String password = scanner.nextLine();
		String hashedpassword=getHash(password);
		System.out.print("Enter phone number: ");
		int phoneNumber = scanner.nextInt();
		System.out.print("Enter age: ");
		int age = scanner.nextInt();
		scanner.nextLine(); // Consume newline character
		System.out.print("Enter gender: ");
		String gender = scanner.nextLine();

		if (isStrongPassword(password)) {
			if (role=="Doctor")
			{
				writeInfoToFile("C:\\Users\\Yanal\\eclipse-workspace\\yanalbawabsc\\", name, hashedpassword, phoneNumber, gender, age,role);
			System.out.println("Successfully registerd");
			}
			else if (role=="Patient")
			{
				writeInfoToFile("C:\\Users\\Yanal\\eclipse-workspace\\yanalbawabsc\\", name, hashedpassword, phoneNumber, gender, age,role);
				System.out.println("Successfully registerd");
			}
		} 
		else {
			System.err.println("Weak password. Please use a stronger password.");
		}
	}
	
    private static void writeInfoToFile(String filepath, String username,String password, int phoneNumber, String gender, int age,String Role) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath+Role+".txt", true))) {
            // Append the new patient information to the file
            writer.write(username + "," +password+","+ phoneNumber + "," + gender + "," + age);
            writer.newLine(); // Move to the next line for the next entry
        } catch (IOException e) {
            System.err.println("Error writing patient information to the file: " + e.getMessage());
        }
    }

	public static void main(String[] args) {
		HospitalManagementSystem hospital = new HospitalManagementSystem();
		Scanner scanner = new Scanner(System.in);
try {
		while (true) {
			System.out.println("Welcome to the Hospital Management System");
			System.out.println("1. Login as Admin");
			System.out.println("2. Login as Doctor");
			System.out.println("3. Login as Patient");
			System.out.println("4. Exit");
			System.out.print("Enter your choice (1/2/3/4): ");
			int choice = scanner.nextInt();
			String usertype;
			scanner.nextLine(); // Consume newline character

			if (choice == 1) {
				System.out.print("Enter admin username: ");
				String adminUsername = scanner.nextLine();
				System.out.print("Enter admin password: ");
				String adminPassword = scanner.nextLine();

				String hashedAdminPass = getHash(adminPassword);

				hospital.setAdminUsername(adminUsername);
				hospital.setAdminPasswordHash(hashedAdminPass);

				if (loadAdminCredentials(hospital.getAdminUsername(), hospital.getAdminPasswordHash())) {
					System.out.println("Admin login successful.");
					hospital.adminMenu();
				} else {
					System.err.println("Admin login failed. Invalid username or password.");
				}

			} else if (choice == 2) {
				// Login as Doctor
				System.out.print("Enter doctor username: ");
				String doctorUsername = scanner.nextLine();
				System.out.print("Enter doctor password: ");
				String doctorPassword = scanner.nextLine();
				String hashedDoctorPass = getHash(doctorPassword);
				usertype = "Doctor";
				if (hospital.Userlogin(doctorUsername, hashedDoctorPass, usertype)) {
					System.out.println("Doctor login successful.");
					doctorMenu(doctorUsername);
				} else {
					System.err.println("Doctor login failed. Invalid username or password.");
				}

			} else if (choice == 3) {
				// Login as Patient
				System.out.print("Enter patient username: ");
				String patientUsername = scanner.nextLine();
				System.out.print("Enter patient password: ");
				String patientPassword = scanner.nextLine();
				String hashedPatientPass = getHash(patientPassword);
				usertype = "Patient";
				if (hospital.Userlogin(patientUsername, hashedPatientPass, usertype)) {
					System.out.println("Patient login successful.");
					patientMenu(patientUsername);
				} else {
					System.err.println("Patient login failed. Invalid username or password.");
				}

			} else if (choice == 4) {
				break;
			} else {
				System.err.println("Invalid choice.");
			}
		}
		}catch(InputMismatchException e){
			System.err.println("Invalid input");
			}
	}

	
	private static void doctorMenu(String username)
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("1-View Doctor information");
		System.out.println("2-enter medical information of patient");
		int choice = sc.nextInt();
		switch (choice) {
		case 1:
			viewDocInfo(username);
			break;
		case 2:
			medicalInfofmationDoc();
			break;

		default:
			System.out.println("invalid input");
		}
	}
	
	private static void viewDocInfo(String username)
	{
		BufferedReader doctor = null;
		try {
			doctor = new BufferedReader(new FileReader("C:\\Users\\Yanal\\eclipse-workspace\\yanalbawabsc\\Doctor.txt"));
			String line;
			while ((line = doctor.readLine()) != null) {
				String[] columns = line.split(",");
				if (columns.length >= 5 && columns[0].equals(username)) {
					String doctorName = columns[0];
					long phoneNumber = Long.parseLong(columns[2]);
					String gender = columns[3];
					int age = Integer.parseInt(columns[4]);

					System.out.println("Doctor Information:");
					System.out.println("Name: " + doctorName);
					System.out.println("Phone Number: " + phoneNumber);
					System.out.println("Gender: " + gender);
					System.out.println("Age: " + age);

					break; // Exit the loop after finding the doctor's information
				}
			}
		} catch (IOException e) {
			System.out.println("error read the file");
		} finally {
			try {
				if (doctor != null) {
					doctor.close();
				}
			} catch (IOException e) {
				System.err.println("Error closing");
			}
		}
		
	}
	
	private static void medicalInfofmationDoc()
	{
		Scanner input = new Scanner(System.in);

		System.out.println("Enter patient's name: ");
		String patientName = input.nextLine();

		System.out.println("Enter medical situation: ");
		String medicalSituation = input.nextLine();

		System.out.println("Enter medical treatment: ");
		String medicalTreatment = input.nextLine();

		// Save the medical information to a file
		writeMedicalInfoToFile("C:\\Users\\Yanal\\eclipse-workspace\\yanalbawabsc\\MedicalInfo.txt",
				 patientName, medicalSituation, medicalTreatment);
		System.out.println("Successful Saved");
	}
	private static void writeMedicalInfoToFile(String fileName, String patientName,
			String medicalSituation, String medicalTreatment) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
			// Append the new medical information to the file
			writer.write(patientName + "," + medicalSituation + "," + medicalTreatment);
			writer.newLine(); // Move to the next line for the next entry
		} catch (IOException e) {
			System.err.println("Error writing medical information to the file: ");
		}
	}
	
	private static void patientMenu(String username)
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("1-View Patient information");
		System.out.println("2-View medical information");
		int choice = sc.nextInt();
	
		if (choice==1) {
			viewPatientInfo(username);
		}
		else if (choice==2){
			medicalInfofmationPatient(username);
		}
		else {
			System.out.println("invalid input");
			}
		
			
		}
	
	private static void viewPatientInfo(String username)
	{
		BufferedReader patient = null;

		try {
			patient = new BufferedReader(new FileReader("C:\\Users\\Yanal\\eclipse-workspace\\yanalbawabsc\\Patient.txt"));
			String line;
			while ((line = patient.readLine()) != null) {
				String[] columns = line.split(",");
				if (columns.length >= 5 && columns[0].equals(username)) {
					String patientName = columns[0];
					long phoneNumber = Long.parseLong(columns[2]);
					String gender = columns[3];
					int age = Integer.parseInt(columns[4]);

					System.out.println("Patient Information:");
					System.out.println("Name: " + patientName);
					System.out.println("Phone Number: " + phoneNumber);
					System.out.println("Gender: " + gender);
					System.out.println("Age: " + age);

					break; // Exit the loop after finding the patient's information
				}
			}
		} catch (IOException e) {
			System.out.println("error read the file");
		} finally {
			try {
				if (patient != null) {
					patient.close();
				}
			} catch (IOException e) {
				System.err.println("Error closing");
			}
		}
	}
	
	private static void medicalInfofmationPatient(String username)
	{
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(
					"C:\\Users\\Yanal\\eclipse-workspace\\yanalbawabsc\\MedicalInfo.txt"));
			String line;
	        while ((line = br.readLine()) != null) {
	            String[] columns = line.split(",");

	            if (columns.length >= 2 && columns[0].equals(username)) {
	                String medicalSituation = columns[1];
	                String medicalTreatment = columns[2];

	                System.out.println("Medical Record:");
	                System.out.println("Patient Name: " + username);
	                System.out.println("Medical Situation: " + medicalSituation);
	                System.out.println("Medical Treatment: " + medicalTreatment);

	                break; // Exit the loop after finding the medical record
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("Error Reading from file");
	    } finally {
	        try {
	            if (br != null) {
	                br.close();
	            }
	        } catch (IOException e) {
	            System.err.println("Error Closing the reader");

			}
		}
	}
}