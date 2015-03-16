DBC (DataBaseClient)
=====================

Structure

    struct dbc_header
    {
        uint32_t magic; // Always 'WDBC'
        uint32_t record_count; // records per file
        uint32_t field_count; // fields per record
        uint32_t record_size; // sum (sizeof (field_type_i)) | 0 <= i < field_count. field_type_i is NOT defined in the files.
        uint32_t string_block_size;
    };

    template<typename record_type>
    struct dbc_file
    {
        dbc_header header;
        // static_assert hreader.record_size == sizeof (record_type));
        record_type records[header.record_count];
        char string_block[header.string_block_size];
    };


String Block

    typedef uint32_t string_offset_type;
    struct example_record
    {
        uint32_t id;
        string_offset_type name;
    }

Printing can be done via both

    // dbc_file<example_record> file;
    printf ("record %u: %u, %s\n", file.records[i].id, file.string_block[file.records[i].name]);

and

    // const char* file;
    uint32_t record_count = *(uint32_t*) (file + 1 * sizeof (uint32_t));
    uint32_t record_size = *(uint32_t*) (file + 3 * sizeof (uint32_t));
    const char* records = file + 5 * sizeof (unit32_t) /* header */;
    const char* string_block = records + record_size * record_count;
    printf ("record %u: %u, %s\", *(uint32_t*)(records + i * record_size /* id */), string_block + *(uint32_t*)(records + i * record_size + sizeof (uint32_t) /* name */));


DB2
====

...