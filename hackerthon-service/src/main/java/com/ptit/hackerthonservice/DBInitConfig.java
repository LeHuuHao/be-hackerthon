package com.ptit.hackerthonservice;


import com.ptit.hackerthonservice.entity.Privilege;
import com.ptit.hackerthonservice.entity.Role;
import com.ptit.hackerthonservice.entity.User;
import com.ptit.hackerthonservice.repository.PrivilegeRepository;
import com.ptit.hackerthonservice.repository.RoleRepository;
import com.ptit.hackerthonservice.repository.UserRepository;
import com.ptit.hackerthonservice.utils.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

@Component
public class DBInitConfig implements ApplicationRunner {

	@Autowired
	private RoleRepository roleDao;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private PrivilegeRepository privilegeRepository;

	public void run(ApplicationArguments args) throws Exception {
		new Thread(() -> {
			ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
			taskScheduler.initialize();
			CountDownLatch latch = new CountDownLatch(1);
			taskScheduler.scheduleAtFixedRate(() -> {
				if (userRepo != null && roleDao != null) {
					latch.countDown();
				}
			}, 100);
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			taskScheduler.destroy();

			RoleEnum[] roleEnums = RoleEnum.class.getEnumConstants();
			for (int i = 0; i < roleEnums.length; i++) {
				Role role = roleDao.findById(roleEnums[i].getRoleId()).orElse(null);
				if (role == null) {
					role = new Role();
					role.setId(roleEnums[i].getRoleId());
					role.setName(roleEnums[i].getRoleName());
					roleDao.save(role);
				} else if (!role.getName().equals(roleEnums[i].getRoleName())) {
					role.setName(roleEnums[i].getRoleName());
					roleDao.save(role);
				}
			}

			User user = userRepo.findByUsername("admin").orElse(null);
			if (user == null) {
				user = new User();
				user.setId(1);
				user.setName("ADMIN");
				user.setUsername("admin");
				user.setPassword(new BCryptPasswordEncoder().encode("123456"));
				user.setEnabled(true);

				Role role = new Role();
				role.setId(RoleEnum.ADMIN.getRoleId());
				user.setRole(role);

				userRepo.save(user);
			}

			Privilege privilege = privilegeRepository.findByAuthority("MEDIA_DOWNLOAD").orElse(null);

			if (privilege == null) {
				privilege = new Privilege();
				privilege.setApi("^/media/download/.+$");
				privilege.setAuthenticated(false);
				privilege.setAuthority("MEDIA_DOWNLOAD");
				privilege.setMethod(HttpMethod.GET);
				privilege.setSecured(false);

				privilegeRepository.save(privilege);
			}


			privilege = privilegeRepository.findByAuthority("USER_ME").orElse(null);

			if (privilege == null) {
				privilege = new Privilege();
				privilege.setApi("^/me$");
				privilege.setAuthenticated(true);
				privilege.setAuthority("USER_ME");
				privilege.setMethod(HttpMethod.GET);
				privilege.setSecured(true);

				privilegeRepository.save(privilege);
			}

			privilege = privilegeRepository.findByAuthority("MEDIA_DOWNLOAD").orElse(null);

			if (privilege == null) {
				privilege = new Privilege();
				privilege.setApi("^/media/download/.+$");
				privilege.setAuthenticated(false);
				privilege.setAuthority("MEDIA_DOWNLOAD");
				privilege.setMethod(HttpMethod.GET);
				privilege.setSecured(false);

				privilegeRepository.save(privilege);
			}


			privilege = privilegeRepository.findByAuthority("REFRESH_TOKEN").orElse(null);


			if (privilege == null) {
				privilege = new Privilege();
				privilege.setApi("^/refresh-token$");
				privilege.setAuthenticated(false);
				privilege.setAuthority("REFRESH_TOKEN");
				privilege.setMethod(HttpMethod.POST);
				privilege.setSecured(false);

				privilegeRepository.save(privilege);
			}

			privilege = privilegeRepository.findByAuthority("SIGN_IN").orElse(null);


			if (privilege == null) {
				privilege = new Privilege();
				privilege.setApi("^/signin$");
				privilege.setAuthenticated(false);
				privilege.setAuthority("SIGN_IN");
				privilege.setMethod(HttpMethod.POST);
				privilege.setSecured(false);

				privilegeRepository.save(privilege);
			}

		}).start();
	}

}
