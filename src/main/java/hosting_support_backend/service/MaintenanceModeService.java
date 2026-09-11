package hosting_support_backend.service;

import hosting_support_backend.entity.AppSetting;
import hosting_support_backend.repository.AppSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MaintenanceModeService {

    public static final String MAINTENANCE_MODE_KEY = "MAINTENANCE_MODE";

    private final AppSettingRepository appSettingRepository;

    public MaintenanceModeService(AppSettingRepository appSettingRepository) {
        this.appSettingRepository = appSettingRepository;
    }

    @Transactional(readOnly = true)
    public boolean isMaintenanceEnabled() {
        return appSettingRepository.findBySettingKey(MAINTENANCE_MODE_KEY)
                .map(setting -> Boolean.parseBoolean(setting.getSettingValue()))
                .orElse(false);
    }

    @Transactional
    public void setMaintenanceEnabled(boolean enabled) {
        AppSetting setting = appSettingRepository.findBySettingKey(MAINTENANCE_MODE_KEY)
                .orElseGet(() -> AppSetting.builder()
                        .settingKey(MAINTENANCE_MODE_KEY)
                        .settingValue("false")
                        .build());
        setting.setSettingValue(String.valueOf(enabled));
        appSettingRepository.save(setting);
    }
}