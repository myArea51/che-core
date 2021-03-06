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
package org.eclipse.che.ide.ui.smartTree.sorting;

import org.eclipse.che.ide.api.project.node.Node;

import java.util.Comparator;

/**
 * @author Vlad Zhukovskiy
 */
public class AlphabeticalFilter implements Comparator<Node> {
    @Override
    public int compare(Node o1, Node o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
