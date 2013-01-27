package com.simple.ged.models;

import com.simple.ged.connector.plugins.getter.SimpleGedGetterPlugin;
import com.simple.ged.connector.plugins.getter.SimpleGedGetterPluginProperty;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Some ged plugin is a container for SimpleGedPlugin, which add many details about the plugin (for management)
 * 
 * This is different of SimpleGedPlugin because this class has an access to the informations in
 * database.
 * 
 * @author xavier
 *
 */
@Entity
@Table(name="plugin")
public class GedGetterPlugin {

    /**
     * Plugin ID
     */
    @Id
    @GeneratedValue
    @Column(name="rowid")
    private Integer id;
	
    /**
     * Plugin's file name
     */
    @Column(name="file_name")
    private String fileName;
 
    /**
     * Last update date
     */
    @Column(name="last_update_date")
    private Date lastUpdateDate;
    
    /**
     * The day of month for update
     */
    @Column(name="day_of_month_for_update")
    private int dayOfMonthForUpdate;
    
    /**
     * The destination directory (relative path)
     */
    @Column(name="destination_directory")
    private String destinationDirectory;
    
    /**
     * The destination file name pattern
     */
    @Column(name="destination_file_pattern")
    private String destinationFilePattern;
    
    /**
     * Interval between updates (in month)
     */
    @Column(name="interval_between_update")
    private int intervalBetweenUpdates;
    
    /**
     * Properties attached to this plugin
     */
    @OneToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="plugin_id", nullable=false)
    private List<SimpleGedGetterPluginProperty> pluginProperties;

    /**
     * The concerned plugin
     */
    @Transient
    private SimpleGedGetterPlugin plugin;

    
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public int getDayOfMonthForUpdate() {
		return dayOfMonthForUpdate;
	}

	public void setDayOfMonthForUpdate(int dayOfMonthForUpdate) {
		this.dayOfMonthForUpdate = dayOfMonthForUpdate;
	}

	public String getDestinationFilePattern() {
		return destinationFilePattern;
	}

	public void setDestinationFilePattern(String destinationFilePattern) {
		this.destinationFilePattern = destinationFilePattern;
	}

	public int getIntervalBetweenUpdates() {
		return intervalBetweenUpdates;
	}

	public void setIntervalBetweenUpdates(int intervalBetweenUpdates) {
		this.intervalBetweenUpdates = intervalBetweenUpdates;
	}

	public List<SimpleGedGetterPluginProperty> getPluginProperties() {
		return pluginProperties;
	}

	public void setPluginProperties(List<SimpleGedGetterPluginProperty> pluginProperties) {
		this.pluginProperties = pluginProperties;
	}

	public String getDestinationDirectory() {
		return destinationDirectory;
	}

	public void setDestinationDirectory(String destinationDirectory) {
		this.destinationDirectory = destinationDirectory;
	}

	public SimpleGedGetterPlugin getPlugin() {
		return plugin;
	}

	public void setPlugin(SimpleGedGetterPlugin plugin) {
		this.plugin = plugin;
	}

	public boolean isActivated() {
		return (id != null);
	}
}
