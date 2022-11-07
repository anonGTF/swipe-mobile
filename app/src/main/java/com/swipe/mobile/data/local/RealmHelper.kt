package com.swipe.mobile.data.local

import io.realm.*

class RealmHelper<T: RealmObject> {
    fun saveData(data: T) {
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction { r -> r.copyToRealmOrUpdate(data) }
        } finally {
            realm?.close()
        }
    }

    fun saveList(data: List<T>?) {
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction { r ->
                if (data != null) {
                    r.copyToRealmOrUpdate(data)
                }
            }
        } finally {
            realm?.close()
        }
    }

    fun saveIdWithList(id: Int, list: List<Any>, data: T) {
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            realm?.executeTransaction { r ->
                val newList: RealmList<Any> = RealmList<Any>()
                newList.addAll(list)
                data.apply {
                    id
                    newList
                }
                r.copyToRealmOrUpdate(data)
            }
        } finally {
            realm?.close()
        }
    }

    fun getData(data: T, params: Map<String, RealmAny> = mapOf()): T? {
        val realm = Realm.getDefaultInstance()
        val query = realm.where(data::class.java)
        params.forEach { (key, value) -> query.equalTo(key, value) }
        val cache = query.findFirst()
        return if (cache != null && cache.isValid) {
            realm.copyFromRealm(cache)
        } else {
            null
        }
    }

    fun getList(
        data: T,
        params: Map<String, RealmAny> = mapOf(),
        distinctField: String? = null,
        sortOrder: Pair<String, Sort>? = null,
        isReturnRealmResult: Boolean = false
    ): List<T>? {
        val realm = Realm.getDefaultInstance()

        val query = realm.where(data::class.java)
        params.forEach { (key, value) -> query.equalTo(key, value) }
        distinctField?.let { query.distinct(distinctField) }
        sortOrder?.let {
            val (fieldName, order) = sortOrder
            query.sort(fieldName, order)
        }

        val cache = query.findAll()

        if (isReturnRealmResult) return cache as RealmResults<out T>

        return if (cache.isNotEmpty() && cache.isValid) {
            realm.copyFromRealm(cache)
        } else {
            null
        }
    }

    fun deleteAll(data: T) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.where(data::class.java).findAll().deleteAllFromRealm()
        realm.commitTransaction()
        realm.close()
    }

    fun delete(id: Int, paramName: String, data: T) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.where(data::class.java).equalTo(paramName, id).findFirst()?.deleteFromRealm()
        realm.commitTransaction()
        realm.close()
    }

    fun dropDatabase() {
        val realm: Realm = Realm.getDefaultInstance()
        realm.use { r ->
            r.executeTransaction { rr ->
                rr.deleteAll()
            }
        }
    }
}