# Generated by Django 3.2.8 on 2021-10-11 06:38

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('maize_api', '0003_alter_data_timestamp'),
    ]

    operations = [
        migrations.AlterField(
            model_name='data',
            name='timestamp',
            field=models.TimeField(),
        ),
    ]
