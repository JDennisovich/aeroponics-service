
package Capstone.Aeroponics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import Capstone.Aeroponics.models.entities.User;
import Capstone.Aeroponics.models.request.jwt.JwtRecord;
import Capstone.Aeroponics.models.request.jwt.RSAKeyRecord;
import Capstone.Aeroponics.repositories.UserRepository;

@EnableConfigurationProperties({
	RSAKeyRecord.class,
	JwtRecord.class
})
@SpringBootApplication
public class AeroponicsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AeroponicsApplication.class, args);
	}

	 @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Start up command line runner.
     *
     * @return the command line runner
     */
//    @Bean
//    public CommandLineRunner startUp() {
//        return _ -> {
//
//            List<User> users = userRepository.findAll();
//
//            if (users.isEmpty()) {
//                User user = User.builder()
//                        .first_name("System")
//                        .last_name("Admin")
//                        .build();
//                userRepository.save(user);
//            }
//        };
//    }

}
