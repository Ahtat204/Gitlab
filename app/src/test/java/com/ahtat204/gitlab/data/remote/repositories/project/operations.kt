package com.ahtat204.gitlab.data.remote.repositories.project

import com.ahtat204.gitlab.data.queries.GetProjectDetailsQuery
import com.apollographql.apollo.api.Adapter
import com.apollographql.apollo.api.CompiledField
import com.apollographql.apollo.api.CustomScalarAdapters
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.json.JsonWriter

class operation: Operation<GetProjectDetailsQuery.Data> {
    override fun document(): String {
        return ""
    }
    override fun name(): String {
        return ""
    }
    override fun id(): String {
        return ""
    }
    override fun adapter(): Adapter<GetProjectDetailsQuery.Data> {
        TODO()
    }
    override fun serializeVariables(
        writer: JsonWriter, customScalarAdapters: CustomScalarAdapters, withDefaultValues: Boolean
    ) {
        TODO("Not yet implemented")
    }
    override fun rootField(): CompiledField {
        TODO("Not yet implemented")
    }
}