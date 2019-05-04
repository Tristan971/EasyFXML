package moe.tristan.easyfxml.natives.settings.model;

import java.util.Optional;

public interface SettingRepository {

    <T> Optional<Setting<T>> getSetting(String settingName, Class<? extends T> valueType);

    void saveSetting(Setting<?> setting);

}
