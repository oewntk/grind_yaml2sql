/*
 * Copyright (c) 2024-2024. Bernard Bou.
 */
package org.oewntk.grind.yaml2sql

import java.io.*

class CustomObjectInputStream(`in`: InputStream, private val classLoader: ClassLoader) : ObjectInputStream(`in`) {

    class CustomClassLoader : ClassLoader() {

        val classes = HashSet<String>()

        @Throws(ClassNotFoundException::class)
        override fun loadClass(name: String): Class<*> {
            classes.add(name)
            // Use the parent class loader to actually load the class
            return super.loadClass(name)
        }
    }

    @Throws(IOException::class, ClassNotFoundException::class)
    override fun resolveClass(desc: ObjectStreamClass): Class<*> {
        try {
            val name = desc.name
            return Class.forName(name, false, classLoader)
        } catch (ex: ClassNotFoundException) {
            return super.resolveClass(desc)
        }
    }
}

object Checker {

    @Throws(IOException::class, ClassNotFoundException::class)
    fun loadClassesOf(file: String): Pair<String, Set<String>> {
        val classLoader = CustomObjectInputStream.CustomClassLoader()
        FileInputStream(file)
            .use {
                CustomObjectInputStream(it, classLoader)
                    .use { ois ->
                        val obj = ois.readObject()
                        val clazz = obj.javaClass.name
                         return clazz to classLoader.classes
                    }
            }
    }
}
