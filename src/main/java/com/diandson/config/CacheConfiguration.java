package com.diandson.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.diandson.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.diandson.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.diandson.domain.User.class.getName());
            createCache(cm, com.diandson.domain.Authority.class.getName());
            createCache(cm, com.diandson.domain.User.class.getName() + ".authorities");
            createCache(cm, com.diandson.domain.Structure.class.getName());
            createCache(cm, com.diandson.domain.Structure.class.getName() + ".packs");
            createCache(cm, com.diandson.domain.Structure.class.getName() + ".medicaments");
            createCache(cm, com.diandson.domain.Structure.class.getName() + ".personnes");
            createCache(cm, com.diandson.domain.Medicament.class.getName());
            createCache(cm, com.diandson.domain.Personne.class.getName());
            createCache(cm, com.diandson.domain.Personne.class.getName() + ".commandes");
            createCache(cm, com.diandson.domain.Personne.class.getName() + ".assurances");
            createCache(cm, com.diandson.domain.Personne.class.getName() + ".ventes");
            createCache(cm, com.diandson.domain.Personne.class.getName() + ".fournisseurs");
            createCache(cm, com.diandson.domain.Personne.class.getName() + ".clients");
            createCache(cm, com.diandson.domain.Personne.class.getName() + ".livraisons");
            createCache(cm, com.diandson.domain.Personne.class.getName() + ".inventaires");
            createCache(cm, com.diandson.domain.Personne.class.getName() + ".messages");
            createCache(cm, com.diandson.domain.Personne.class.getName() + ".notifications");
            createCache(cm, com.diandson.domain.Pack.class.getName());
            createCache(cm, com.diandson.domain.TypePack.class.getName());
            createCache(cm, com.diandson.domain.TypePack.class.getName() + ".packs");
            createCache(cm, com.diandson.domain.Commande.class.getName());
            createCache(cm, com.diandson.domain.Commande.class.getName() + ".commandeMedicaments");
            createCache(cm, com.diandson.domain.CommandeMedicament.class.getName());
            createCache(cm, com.diandson.domain.Assurance.class.getName());
            createCache(cm, com.diandson.domain.Livraison.class.getName());
            createCache(cm, com.diandson.domain.Vente.class.getName());
            createCache(cm, com.diandson.domain.Vente.class.getName() + ".venteMedicaments");
            createCache(cm, com.diandson.domain.VenteMedicament.class.getName());
            createCache(cm, com.diandson.domain.Fournisseur.class.getName());
            createCache(cm, com.diandson.domain.Inventaire.class.getName());
            createCache(cm, com.diandson.domain.Inventaire.class.getName() + ".inventaireMedicaments");
            createCache(cm, com.diandson.domain.InventaireMedicament.class.getName());
            createCache(cm, com.diandson.domain.Client.class.getName());
            createCache(cm, com.diandson.domain.Notification.class.getName());
            createCache(cm, com.diandson.domain.Message.class.getName());
            createCache(cm, com.diandson.domain.Approvisionnement.class.getName());
            createCache(cm, com.diandson.domain.Approvisionnement.class.getName() + ".approvisionnementMedicaments");
            createCache(cm, com.diandson.domain.ApprovisionnementMedicament.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
