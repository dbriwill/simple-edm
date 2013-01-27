package com.simple.ged.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Index;



/**
 * Some directory in the library
 * 
 * @author xavier
 *
 */
@Entity
@Table(name="ged_directory")
public class GedDirectory {

	/**
	 * The ID
	 */
    @Id
    @GeneratedValue
    @Column(name="rowid")
	private Integer id;
	
	/**
	 * The directory path, relative to the library root
	 */
    @Column(name="path")
    @Index(name="idx_directory_file_path")
	private String relativeDirectoryPath;

	/**
	 * The icon file path
	 */
    @Column(name="icon_path")
	private String iconPath;
	
	
	public GedDirectory() {
		id = null;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRelativeDirectoryPath() {
		return relativeDirectoryPath;
	}

	public void setRelativeDirectoryPath(String relativeDirectoryPath) {
		this.relativeDirectoryPath = relativeDirectoryPath;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GedDirectory other = (GedDirectory) obj;
        return id == other.id;
    }
}
