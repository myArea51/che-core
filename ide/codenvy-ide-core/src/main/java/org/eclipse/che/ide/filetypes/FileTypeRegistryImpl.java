/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.filetypes;

import org.eclipse.che.ide.api.filetypes.FileType;
import org.eclipse.che.ide.api.filetypes.FileTypeRegistry;
import org.eclipse.che.ide.api.project.tree.VirtualFile;
import org.eclipse.che.ide.collections.Array;
import org.eclipse.che.ide.collections.Collections;
import com.google.gwt.regexp.shared.RegExp;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Implementation of {@link org.eclipse.che.ide.api.filetypes.FileTypeRegistry}
 *
 * @author Artem Zatsarynnyy
 */
@Singleton
public class FileTypeRegistryImpl implements FileTypeRegistry {
    private final FileType        unknownFileType;
    private final Array<FileType> fileTypes;

    @Inject
    public FileTypeRegistryImpl(@Named("defaultFileType") FileType unknownFileType) {
        this.unknownFileType = unknownFileType;
        fileTypes = Collections.createArray();
    }

    @Override
    public void registerFileType(FileType fileType) {
        fileTypes.add(fileType);
    }

    @Override
    public Array<FileType> getRegisteredFileTypes() {
        return Collections.createArray(fileTypes.asIterable());
    }

    @Override
    public FileType getFileTypeByFile(VirtualFile file) {
        FileType fileType = getFileTypeByNamePattern(file.getName());
        if (fileType == unknownFileType) {
            fileType = getFileTypeByMimeType(file.getMediaType());
        }
        if (fileType == unknownFileType) {
            fileType = getFileTypeByExtension(getFileExtension(file.getName()));
        }
        return fileType != null ? fileType : unknownFileType;
    }

    @Override
    public FileType getFileTypeByExtension(String extension) {
        for (FileType type : fileTypes.asIterable()) {
            if (extension.equals(type.getExtension())) {
                return type;
            }
        }
        return unknownFileType;
    }

    @Override
    public FileType getFileTypeByMimeType(String mimeType) {
        for (FileType type : fileTypes.asIterable()) {
            if (type.getMimeTypes().contains(mimeType)) {
                return type;
            }
        }
        return unknownFileType;
    }

    @Override
    public FileType getFileTypeByNamePattern(String name) {
        for (FileType type : fileTypes.asIterable()) {
            if (type.getNamePattern() != null) {
                RegExp regExp = RegExp.compile(type.getNamePattern());
                if (regExp.test(name)) {
                    return type;
                }
            }
        }
        return unknownFileType;
    }

    private String getFileExtension(String name) {
        final int lastDotPosition = name.lastIndexOf('.');
        // name has no extension
        if (lastDotPosition < 0) {
            return "";
        }
        return name.substring(lastDotPosition + 1);
    }
}
