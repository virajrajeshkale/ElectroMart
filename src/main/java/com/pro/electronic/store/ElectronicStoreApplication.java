package com.pro.electronic.store;

import com.pro.electronic.store.entites.Role;
import com.pro.electronic.store.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {
  @Autowired
	private PasswordEncoder passwordEncoder;

  @Autowired
  private RoleRepository roleRepository;

  @Value("${admin.role.id}")
  private  String Admin_role_id;

  @Value("${normal.role.id}")
  private  String Normal_role_id;

	public static void main(String[] args) {

		SpringApplication.run(ElectronicStoreApplication.class, args);
		System.out.println("Application Started..");
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(passwordEncoder.encode("pasbsjs"));
			System.out.println(passwordEncoder.encode("shiv@124"));
		System.out.println(passwordEncoder.encode("pahbsjs"));


		try
		{
			Role role_admin = Role.builder().roleId(Admin_role_id).roleName("ROLE_ADMIN").build();
			Role role_normal = Role.builder().roleId(Normal_role_id).roleName("ROLE_NORMAL").build();

				roleRepository.save(role_admin);
				roleRepository.save(role_normal);
		}catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
