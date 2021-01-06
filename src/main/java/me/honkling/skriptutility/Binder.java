package me.honkling.skriptutility;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class Binder extends AbstractModule {

    private final SkriptUtility plugin;

    public Binder(SkriptUtility plugin) {
        this.plugin = plugin;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        this.bind(SkriptUtility.class).toInstance(this.plugin);
    }

}
