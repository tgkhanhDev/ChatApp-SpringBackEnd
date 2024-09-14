package com.chatApp.chatApp.config;

import com.chatApp.chatApp.enums.EPermission;
import com.chatApp.chatApp.enums.ERole;
import com.chatApp.chatApp.model.Account;
import com.chatApp.chatApp.model.Permission;
import com.chatApp.chatApp.model.Role;
import com.chatApp.chatApp.repositories.AccountRepository;
import com.chatApp.chatApp.repositories.PermissionRepository;
import com.chatApp.chatApp.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ApplicationConfig {
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    @ConditionalOnProperty(prefix = "spring.data.mongodb", value = "driverClassName", havingValue = "org.mongodb.driver")
    ApplicationRunner applicationRunner(AccountRepository accountRepository,
                                        PermissionRepository permissionRepository,
                                        RoleRepository roleRepository) {
        return args -> {

            log.info("Initialize...");
            //Create default permissions
            createDefaultPermission(permissionRepository);

            //create default roles
            createDefaultRole(roleRepository, permissionRepository);

            if(accountRepository.existsByUsername("SADMIN")) {
                return;
            }

            Account SAdmin = Account.builder()
                    .username("SADMIN")
                    .password(passwordEncoder.encode("170504"))
                    .roles(Set.of(ERole.SADMIN.toString(), ERole.ADMIN.toString(), ERole.GUEST.toString()))
                    .dob(new Date(20 - 8 - 2000))
                    .firstName("ADMIN")
                    .lastName("CUTE")
                    .build();
            accountRepository.save(SAdmin);
            log.warn("SADMIN user has been created with default password: 170504, please change it later");

        };
    }

    private void createDefaultPermission(PermissionRepository permissionRepository) {
        //[VIEW_CHAT VIEW_INFO UPDATE_ACCOUNT], [ CR_ACCOUNT CRUD_CHAT ], [UD_ACCOUNT CRUD_ROLE CRUD_PERMISSION]
        final String[] PERMISSIONS = {"VIEW_CHAT", "VIEW_INFO", "UPDATE_ACCOUNT", "CR_ACCOUNT", "CRUD_CHAT", "UD_ACCOUNT", "CRUD_ROLE", "CRUD_PERMISSION"};

        Arrays.stream(PERMISSIONS).forEach((per) -> {
            if (!permissionRepository.existsByName(per)) {
                Permission permission = Permission.builder()
                        .name(per)
                        .build();
                permissionRepository.save(permission);
                log.warn("Default permission {} has been created", per);
            }
        });
    }

    private void createDefaultRole(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        // GUEST, ADMIN, SADMIN
        final Set<String> PERMISSIONS_GUEST =
                Set.of(EPermission.VIEW_CHAT.toString(),
                        EPermission.VIEW_INFO.toString()
                );
        final Set<String> PERMISSIONS_ADMIN =
                Set.of(EPermission.UPDATE_ACCOUNT.toString(),
                        EPermission.CR_ACCOUNT.toString(),
                        EPermission.CRUD_CHAT.toString());

        final Set<String> PERMISSIONS_SUPER_ADMIN =
                Set.of(EPermission.UD_ACCOUNT.toString(),
                        EPermission.CRUD_ROLE.toString(),
                        EPermission.CRUD_PERMISSION.toString());


        if (!roleRepository.existsByName(ERole.GUEST.toString())) {
            Role role = Role.builder()
                    .name(ERole.GUEST.toString())
                    .permissions((PERMISSIONS_GUEST.stream().map(permissionRepository::findByName)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toSet())))
                    .build();
            roleRepository.save(role);
            log.warn("Default role {} has been created", role);
        }

        if (!roleRepository.existsByName(ERole.ADMIN.toString())) {
            Role role = Role.builder()
                    .name(ERole.ADMIN.toString())
                    .permissions((PERMISSIONS_ADMIN.stream().map(permissionRepository::findByName)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toSet())))
                    .build();
            roleRepository.save(role);
            log.warn("Default role {} has been created", role);
        }

        if (!roleRepository.existsByName(ERole.SADMIN.toString())) {
            Role role = Role.builder()
                    .name(ERole.SADMIN.toString())
                    .permissions((PERMISSIONS_SUPER_ADMIN.stream().map(permissionRepository::findByName)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toSet())))
                    .build();
            roleRepository.save(role);
            log.warn("Default role {} has been created", role);
        }

    }
}
