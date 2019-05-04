package moe.tristan.easyfxml.natives.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import moe.tristan.easyfxml.natives.settings.model.SettingRepository;

@Component
public class SettingsService {

    private final SettingRepository userSettingsRepository;
    private final SettingRepository systemSettingsRepository;

    @Autowired
    public SettingsService(SettingRepository userSettingsRepository, SettingRepository systemSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
        this.systemSettingsRepository = systemSettingsRepository;
    }

}
