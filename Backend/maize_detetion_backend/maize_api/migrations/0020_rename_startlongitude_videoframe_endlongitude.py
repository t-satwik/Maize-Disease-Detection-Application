# Generated by Django 3.2.8 on 2021-11-03 20:31

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('maize_api', '0019_videoframe_time_stamp'),
    ]

    operations = [
        migrations.RenameField(
            model_name='videoframe',
            old_name='startLongitude',
            new_name='endLongitude',
        ),
    ]
