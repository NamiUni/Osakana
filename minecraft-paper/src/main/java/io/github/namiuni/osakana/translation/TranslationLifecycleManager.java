package io.github.namiuni.osakana.translation;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.github.namiuni.osakana.configuration.configurations.PrimaryConfig;
import io.github.namiuni.osakana.spi.OsakanaLifecycleManager;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationStore;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class TranslationLifecycleManager implements OsakanaLifecycleManager {

    private final ComponentLogger logger;
    private final PrimaryConfig primaryConfig;
    private final TranslationStore.StringBased<?> translationStore;
    private final Provider<ResourceBundle.Control> bundleControl;

    @Inject
    private TranslationLifecycleManager(
            final ComponentLogger logger,
            final PrimaryConfig primaryConfig,
            final TranslationStore.StringBased<?> translationStore,
            final Provider<ResourceBundle.Control> bundleControl
    ) {
        this.logger = logger;
        this.primaryConfig = primaryConfig;
        this.translationStore = translationStore;
        this.bundleControl = bundleControl;
    }

    @Override
    public void startup() {
        // Set default locale
        this.translationStore.defaultLocale(this.primaryConfig.defaultLocale());

        // Register resource bundles
        final List<ResourceBundle> bundles = this.createBundles();
        bundles.forEach(bundle -> this.translationStore.registerAll(bundle.getLocale(), bundle, false));
        final String localeNames = this.localeNames(bundles);
        this.logger.info("Loaded {} translations: [{}]", bundles.size(), localeNames);

        // Add Global source
        GlobalTranslator.translator().addSource(this.translationStore);
        this.logger.info("Add Translator to global sources: {}", this.translationStore.name());
    }

    @Override
    public void shutdown() {
        if (GlobalTranslator.translator().removeSource(this.translationStore)) {
            this.logger.info("Remove Translator from global sources: {}", this.translationStore.name());
        }
    }

    private List<ResourceBundle> createBundles() {
        final String baseName = MessageService.class.getAnnotation(io.github.namiuni.doburoku.annotation.annotations.ResourceBundle.class).baseName();
        final ResourceBundle.Control control = this.bundleControl.get();
        return Locale.availableLocales()
                .map(locale -> ResourceBundle.getBundle(baseName, locale, control))
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    private String localeNames(final List<ResourceBundle> bundles) {
        return bundles.stream()
                .map(ResourceBundle::getLocale)
                .map(Object::toString)
                .collect(Collectors.joining(", "));
    }
}
