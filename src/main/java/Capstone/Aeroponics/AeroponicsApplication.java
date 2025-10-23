package Capstone.Aeroponics;

import java.util.List;
import java.util.Arrays;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import Capstone.Aeroponics.models.entities.User;
import Capstone.Aeroponics.models.entities.Plant;
import Capstone.Aeroponics.models.entities.About;
import Capstone.Aeroponics.models.entities.Faq;
import Capstone.Aeroponics.models.request.jwt.JwtRecord;
import Capstone.Aeroponics.models.request.jwt.RSAKeyRecord;
import Capstone.Aeroponics.repositories.UserRepository;
import Capstone.Aeroponics.repositories.PlantRepository;
import Capstone.Aeroponics.repositories.AboutRepository;
import Capstone.Aeroponics.repositories.FaqRepository;

@EnableConfigurationProperties({
		RSAKeyRecord.class,
		JwtRecord.class
})
@EnableJpaAuditing
@SpringBootApplication
public class AeroponicsApplication {

	public static void main(String[] args) {
		// Load .env from project root and set system properties when they are not already set.
		Path envPath = Paths.get(System.getProperty("user.dir"), ".env");
		if (Files.exists(envPath)) {
			try (Stream<String> lines = Files.lines(envPath)) {
				lines.map(String::trim)
					 .filter(l -> !l.isEmpty() && !l.startsWith("#"))
					 .forEach(l -> {
						 int eq = l.indexOf('=');
						 if (eq > 0) {
							 String key = l.substring(0, eq).trim();
							 String val = l.substring(eq + 1).trim();
							 if ((val.startsWith("\"") && val.endsWith("\"")) || (val.startsWith("'") && val.endsWith("'"))) {
								 val = val.substring(1, val.length() - 1);
							 }
							 if (System.getProperty(key) == null && System.getenv(key) == null) {
								 System.setProperty(key, val);
							 }
						 }
					 });
			} catch (IOException e) {
				System.out.println("Warning: failed to read .env file at " + envPath + ": " + e.getMessage());
			}
		}

		SpringApplication.run(AeroponicsApplication.class, args);
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PlantRepository plantRepository;

	@Autowired
	private AboutRepository aboutRepository;

	@Autowired
	private FaqRepository faqRepository;

	/**
	 * Start up command line runner.
	 *
	 * @return the command line runner
	 */
	@Bean
	public CommandLineRunner startUp() {
		return args -> {

			List<User> users = userRepository.findAll();

			User user;
			if (users.isEmpty()) {
				user = User.builder()
						.first_name("System")
						.email("admin@example.com")
						.last_name("Admin")
						.password(passwordEncoder.encode("Testing123!"))
						.build();
				user = userRepository.save(user);
			} else {
				user = users.get(0);
			}

			if (plantRepository.count() == 0) {
				List<Plant> defaults = Arrays.asList(
						Plant.builder().user(user).name("Arugula").min_ph_level(new BigDecimal("5.5")).max_ph_level(new BigDecimal("6.8")).min_ppm(560).max_ppm(980).build(),
						Plant.builder().user(user).name("Basil").min_ph_level(new BigDecimal("5.5")).max_ph_level(new BigDecimal("6.5")).min_ppm(700).max_ppm(1120).build(),
						Plant.builder().user(user).name("Bean").min_ph_level(new BigDecimal("6.0")).max_ph_level(new BigDecimal("6.5")).min_ppm(1400).max_ppm(1680).build(),
						Plant.builder().user(user).name("Bok Choy").min_ph_level(new BigDecimal("6.5")).max_ph_level(new BigDecimal("7.0")).min_ppm(1050).max_ppm(1400).build(),
						Plant.builder().user(user).name("Broccoli").min_ph_level(new BigDecimal("6.0")).max_ph_level(new BigDecimal("6.5")).min_ppm(1960).max_ppm(2450).build(),
						Plant.builder().user(user).name("Brussel Sprouts").min_ph_level(new BigDecimal("6.5")).max_ph_level(new BigDecimal("7.5")).min_ppm(1750).max_ppm(2100).build(),
						Plant.builder().user(user).name("Bunching Onion").min_ph_level(new BigDecimal("5.5")).max_ph_level(new BigDecimal("6.8")).min_ppm(1260).max_ppm(1680).build(),
						Plant.builder().user(user).name("Cabbage").min_ph_level(new BigDecimal("6.5")).max_ph_level(new BigDecimal("7.0")).min_ppm(1750).max_ppm(2100).build(),
						Plant.builder().user(user).name("Cauliflower").min_ph_level(new BigDecimal("6.0")).max_ph_level(new BigDecimal("7.0")).min_ppm(1050).max_ppm(1400).build(),
						Plant.builder().user(user).name("Celery").min_ph_level(new BigDecimal("6.3")).max_ph_level(new BigDecimal("6.7")).min_ppm(1260).max_ppm(1680).build(),
						Plant.builder().user(user).name("Chamomile").min_ph_level(new BigDecimal("5.5")).max_ph_level(new BigDecimal("6.5")).min_ppm(560).max_ppm(980).build(),
						Plant.builder().user(user).name("Chives").min_ph_level(new BigDecimal("6.0")).max_ph_level(new BigDecimal("6.5")).min_ppm(1260).max_ppm(1680).build(),
						Plant.builder().user(user).name("Cilantro").min_ph_level(new BigDecimal("6.5")).max_ph_level(new BigDecimal("6.7")).min_ppm(910).max_ppm(1260).build(),
						Plant.builder().user(user).name("Collard Greens").min_ph_level(new BigDecimal("5.5")).max_ph_level(new BigDecimal("6.8")).min_ppm(1120).max_ppm(1750).build(),
						Plant.builder().user(user).name("Cucumber").min_ph_level(new BigDecimal("5.8")).max_ph_level(new BigDecimal("6.0")).min_ppm(1190).max_ppm(1750).build()
				);
				plantRepository.saveAll(defaults);
			}

			if (aboutRepository.count() == 0) {
				About about = About.builder()
						.title("About")
						.content("Our Aeroponics Monitoring and Management System is built to help automate, track, and optimize plant growth conditions. With seamless integration of IoT hardware, backend services, and a modern user dashboard, it provides real-time insights to improve efficiency and sustainability in agriculture.")
						.build();
				aboutRepository.save(about);
			}

			if (faqRepository.count() == 0) {
				List<Faq> faqs = Arrays.asList(
						Faq.builder().question("What is this system about?").answer("This system is built to help automate, track, and optimize plant growth conditions.").build(),
						Faq.builder().question("Who can use this system?").answer("The system can be used by farmers, researchers, and anyone interested in monitoring and managing plant growth.").build(),
						Faq.builder().question("How does the system collect data?").answer("The system collects data through IoT devices connected to the aeroponics setup.").build(),
						Faq.builder().question("Is the data stored securely?").answer("Yes, all collected data is stored securely in the database with proper safeguards.").build(),
						Faq.builder().question("Can I customize the settings?").answer("Yes. You can adjust watering periods, nutrient levels, and other preferences directly from the dashboard.").build()
				);
				faqRepository.saveAll(faqs);
			}
		};
	}

}