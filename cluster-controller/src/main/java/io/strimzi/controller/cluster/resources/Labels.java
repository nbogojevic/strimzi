/*
 * Copyright 2018, Strimzi authors.
 * License: Apache License 2.0 (see the file LICENSE or http://apache.org/licenses/LICENSE-2.0.html).
 */
package io.strimzi.controller.cluster.resources;

import io.fabric8.kubernetes.api.model.HasMetadata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableMap;

/**
 * An immutable set of labels
 */
public class Labels {

    public static final String STRIMZI_DOMAIN = "strimzi.io";
    public static final String STRIMZI_KIND_LABEL = STRIMZI_DOMAIN + "/kind";
    public static final String STRIMZI_TYPE_LABEL = STRIMZI_DOMAIN + "/type";
    public static final String STRIMZI_CLUSTER_LABEL = STRIMZI_DOMAIN + "/cluster";
    public static final String STRIMZI_NAME_LABEL = STRIMZI_DOMAIN + "/name";

    private static final Set<String> STRIMZI_LABELS = new HashSet(asList(STRIMZI_KIND_LABEL, STRIMZI_TYPE_LABEL, STRIMZI_CLUSTER_LABEL, STRIMZI_NAME_LABEL));

    /**
     * The empty set of labels.
     */
    public static final Labels EMPTY = new Labels(emptyMap());

    private final Map<String, String> labels;

    /**
     * Returns the value of the {@code strimzi.io/cluster} label of the given {@code resource}.
     */
    public static String clusterLabel(HasMetadata resource) {
        return resource.getMetadata().getLabels().get(Labels.STRIMZI_CLUSTER_LABEL);
    }

    /**
     * Returns the value of the {@code strimzi.io/type} label of the given {@code resource}.
     */
    public static String typeLabel(HasMetadata resource) {
        return resource.getMetadata().getLabels().get(Labels.STRIMZI_TYPE_LABEL);
    }

    /**
     * Returns the value of the {@code strimzi.io/name} label of the given {@code resource}.
     */
    public static String nameLabel(HasMetadata resource) {
        return resource.getMetadata().getLabels().get(Labels.STRIMZI_NAME_LABEL);
    }

    /**
     * Returns the value of the {@code strimzi.io/kind} label of the given {@code resource}.
     */
    public static String kindLabel(HasMetadata resource) {
        return resource.getMetadata().getLabels().get(Labels.STRIMZI_KIND_LABEL);
    }

    public static Labels userLabels(Map<String, String> userLabels) {
        for (String key : userLabels.keySet()) {
            if (STRIMZI_LABELS.contains(key)) {
                //throw new IllegalArgumentException("User labels includes a Strimzi label: " + key);
            }
        }
        return new Labels(userLabels);
    }

    /**
     * Returns the labels of the given {@code resource}.
     */
    public static Labels fromResource(HasMetadata resource) {
        return new Labels(resource.getMetadata().getLabels());
    }

    private Labels(Map<String, String> labels) {
        this.labels = unmodifiableMap(new HashMap(labels));
    }

    private Labels with(String label, String value) {
        Map<String, String> newLabels = new HashMap<>(labels.size()+1);
        newLabels.putAll(labels);
        newLabels.put(label, value);
        return new Labels(newLabels);
    }

    private Labels without(String label) {
        Map<String, String> newLabels = new HashMap<>(labels);
        newLabels.remove(label);
        return new Labels(newLabels);
    }

    /**
     * The same labels as this instance, but with the given {@code type} for the {@code strimzi.io/type} key.
     */
    public Labels withType(String type) {
        return with(STRIMZI_TYPE_LABEL, type);
    }

    /**
     * The same labels as this instance, but without any {@code strimzi.io/type} key.
     */
    public Labels withoutType() {
        return without(STRIMZI_TYPE_LABEL);
    }

    /**
     * The same labels as this instance, but with the given {@code kind} for the {@code strimzi.io/kind} key.
     */
    public Labels withKind(String kind) {
        return with(STRIMZI_KIND_LABEL, kind);
    }

    /**
     * The same labels as this instance, but with the given {@code cluster} for the {@code strimzi.io/cluster} key.
     */
    public Labels withCluster(String cluster) {
        return with(STRIMZI_CLUSTER_LABEL, cluster);
    }

    /**
     * The same labels as this instance, but with the given {@code name} for the {@code strimzi.io/name} key.
     */
    public Labels withName(String name) {
        return with(STRIMZI_NAME_LABEL, name);
    }

    /**
     * An unmodifiable map of the labels.
     */
    public Map<String, String> toMap() {
        return labels;
    }

    /**
     * A singleton instance with the given {@code cluster} for the {@code strimzi.io/cluster} key.
     */
    public static Labels cluster(String cluster) {
        return new Labels(singletonMap(STRIMZI_CLUSTER_LABEL, cluster));
    }

    /**
     * A singleton instance with the given {@code type} for the {@code strimzi.io/type} key.
     */
    public static Labels type(String type) {
        return new Labels(singletonMap(STRIMZI_TYPE_LABEL, type));
    }

    /**
     * A singleton instance with the given {@code kind} for the {@code strimzi.io/kind} key.
     */
    public static Labels kind(String kind) {
        return new Labels(singletonMap(STRIMZI_KIND_LABEL, kind));
    }

    /**
     * Return the value of the {@code strimzi.io/type}.
     */
    public String type() {
        return labels.get(STRIMZI_TYPE_LABEL);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Labels labels1 = (Labels) o;
        return Objects.equals(labels, labels1.labels);
    }

    @Override
    public int hashCode() {

        return Objects.hash(labels);
    }

    @Override
    public String toString() {
        return "Labels" + labels;
    }
}
