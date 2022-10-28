package ca.utoronto.utm.mcs;

import dagger.Component;
import javax.inject.Singleton;

@Singleton

@Component(modules = ServerModule.class)
public interface ServerComponent {

	public Server buildServer();
}
