Mapping structure
=================
The DBC mapping parser expects the following structure for a mapping:

    {
      "filename": <String>,
      "targetClass": <String>,
      "dbcType": <String>,
      "mapping": String,
      "fields": [
        {
          "name": <String>,
          "column": <Integer>,
          "type": <String>,
          "entries": <Integer>,
          "length": <Integer>,
          "padding": <Boolean>,
          "knownMeaning": <Boolean>,
          "endianness": <String>,
          "references": <String>
        }...
      ]
    }

Mapping properties
==================

Root Object
-----------
<table>
  <thead>
    <th>Property</th>
    <th>Description</th>
    <th>Data Type</th>
    <th>Mandatory</th>
    <th>Value restrictions</th>
    <th>Default value</th>
  </thead>
  <tr>
    <td>filename</td>
    <td>Relative name of the file calculated from the data directory (e.g. DBFilesClient/Achievement_Category.dbc)</td>
    <td>string</td>
    <td>true</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>targetClass</td>
    <td>The fully qualified name of the class the DBC file maps to</td>
    <td>string</td>
    <td>true</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>dbcType</td>
    <td>The DBC type of the class</td>
    <td>string</td>
    <td>true</td>
    <td>Value must be present in nl.salp.warcraft4j.clientdata.dbc.DbcType</td>
    <td></td>
  </tr>
  <tr>
    <td>mapping</td>
    <td>The method for mapping the values to the entries</td>
    <td>string</td>
    <td>true</td>
    <td>"field"</td>
    <td>One of: "field", "constructor", "method"</td>
  </tr>
  <tr>
    <td>fields</td>
    <td>The field mappings</td>
    <td>array of field objects</td>
    <td>true</td>
    <td></td>
    <td></td>
  </tr>
</table>

Field Object
------------
<table>
  <thead>
    <th>Property</th>
    <th>Description</th>
    <th>Data Type</th>
    <th>Mandatory</th>
    <th>Value restrictions</th>
    <th>Default value</th>
  </thead>
  <tr>
    <td>name</td>
    <td>The name of the field on the mapped Java type</td>
    <td>string</td>
    <td>mandatory for non-padding fields, ignored for padding fields</td>
    <td></td>
    <td></td>
  </tr>
  <tr>
    <td>column</td>
    <td>The column index of the data in the DBC file</td>
    <td>integer</td>
    <td>true</td>
    <td>&gt; 0</td>
    <td></td>
  </tr>
  <tr>
    <td>type</td>
    <td>The data type of the entries</td>
    <td>string</td>
    <td>true</td>
    <td>One of: byte, uint8, int16, uint16, int32, uint32, int64, uint64, boolean, string, stringtable_reference, float, double</td>
    <td></td>
  </tr>
  <tr>
    <td>entries</td>
    <td>The number of entries where any entry &gt; 1 gets converted to an array</td>
    <td>integer</td>
    <td>false</td>
    <td>0 &lt; value &lt; 2^32-1</td>
    <td>1</td>
  </tr>
  <tr>
    <td>length</td>
    <td>The length in bytes of the data in the DBC file where a length of 0 uses the default data type length or variable length</td>
    <td>integer</td>
    <td>false</td>
    <td></td>
    <td>0</td>
  </tr>
  <tr>
    <td>padding</td>
    <td>Indicator if the field is a padding field and is not mapped</td>
    <td>boolean</td>
    <td>false</td>
    <td></td>
    <td>false</td>
  </tr>
  <tr>
    <td>knownMeaning</td>
    <td>Indicator that implies if the meaning of the field has been analysed and is known</td>
    <td>boolean</td>
    <td>false</td>
    <td></td>
    <td>true</td>
  </tr>
  <tr>
    <td>endianness</td>
    <td>The endianness of the data in the DBC file</td>
    <td>string</td>
    <td>false</td>
    <td>One of: default, big, little</td>
    <td>default</td>
  </tr>
  <tr>
    <td>references</td>
    <td>The DBC type of the type the field references</td>
    <td>string</td>
    <td>false</td>
    <td>Value must be present in nl.salp.warcraft4j.clientdata.dbc.DbcType</td>
    <td></td>
  </tr>
</table>